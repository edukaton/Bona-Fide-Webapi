package models
import scalikejdbc._
import play.db.Database
import scalikejdbc.DB
import scalikejdbc.interpolation.SQLSyntax

/**
  * Created by zdoonio on 17.02.18.
  */
case class QuestionData(var title: String, var imgSrc: String, var list: List[NewsQuestionData])

case class NewsQuestionData(
  var name: String,
  var answer1: String,
  var answer2: String,
  var answer3: String,
  var answer4: String,
  var correct: Int
)

object QuestionFinder {

  def getQuestionsByNews(news: News): List[Question] = {
    val questions = Question.findAll()
    questions.filter(_.newsId == news.newsId)
  }
}

