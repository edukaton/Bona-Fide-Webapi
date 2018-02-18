package controllers

import javax.inject._

import models._
import play.api.libs.json.Json
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def getQuestionsByNews = Action { implicit request =>
    val allNews = News.findAll()
    val filteredQuestions = allNews.map { info =>
      QuestionFinder.getQuestionsByNews(info)
    }
    val newsQuestionData = filteredQuestions.map { questions =>
      questions.map { question =>
        NewsQuestionData(question.name.getOrElse(""), question.answerOne.getOrElse(""), question.answerTwo.getOrElse(""),
          question.answerThree.getOrElse(""), question.answerFour.getOrElse(""), question.correct.getOrElse(0))
      }
    }.headOption

    val questionData = allNews.map { info =>
      QuestionData(info.title, info.href, newsQuestionData.getOrElse(List()))
    }

    val json = Json.obj("questionsByNews" -> questionData.map { data =>
      Json.obj("title" -> data.title,
        "imgHref" -> data.imgSrc,
        "questions" -> data.list.map { question =>
        Json.obj("question" -> question.name,
        "answerOne"-> question.answer1,
        "answerTwo" -> question.answer2,
        "answerThree" -> question.answer3,
        "answerFour" -> question.answer4,
        "correctAnswer" -> question.correct)
      })
    })
    Ok(json)
  }
}
