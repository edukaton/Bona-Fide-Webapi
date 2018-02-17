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
  var question1: String,
  var question2: String,
  var question3: String,
  var question4: String,
  var correct: Int
)

object QuestionFinder {

  def getQuestionsByNews(news: News): List[Question] = {
    val questions = Question.findAll()
    questions.filter(_.newsId == news.newsId)
  }
}

