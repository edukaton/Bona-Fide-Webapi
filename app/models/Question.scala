package models

import scalikejdbc._

case class Question(
  questionId: Int,
  name: Option[String] = None,
  answerOne: Option[String] = None,
  answerTwo: Option[String] = None,
  answerThree: Option[String] = None,
  answerFour: Option[String] = None,
  correct: Option[Int] = None,
  newsId: Int) {

  def save()(implicit session: DBSession = Question.autoSession): Question = Question.save(this)(session)

  def destroy()(implicit session: DBSession = Question.autoSession): Int = Question.destroy(this)(session)

}


object Question extends SQLSyntaxSupport[Question] {

  override val schemaName = Some("public")

  override val tableName = "question"

  override val columns = Seq("question_id", "name", "answer_one", "answer_two", "answer_three", "answer_four", "correct", "news_id")

  def apply(q: SyntaxProvider[Question])(rs: WrappedResultSet): Question = apply(q.resultName)(rs)
  def apply(q: ResultName[Question])(rs: WrappedResultSet): Question = new Question(
    questionId = rs.get(q.questionId),
    name = rs.get(q.name),
    answerOne = rs.get(q.answerOne),
    answerTwo = rs.get(q.answerTwo),
    answerThree = rs.get(q.answerThree),
    answerFour = rs.get(q.answerFour),
    correct = rs.get(q.correct),
    newsId = rs.get(q.newsId)
  )

  val q = Question.syntax("q")

  override val autoSession = AutoSession

  def find(questionId: Int)(implicit session: DBSession = autoSession): Option[Question] = {
    withSQL {
      select.from(Question as q).where.eq(q.questionId, questionId)
    }.map(Question(q.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Question] = {
    withSQL(select.from(Question as q)).map(Question(q.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Question as q)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Question] = {
    withSQL {
      select.from(Question as q).where.append(where)
    }.map(Question(q.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Question] = {
    withSQL {
      select.from(Question as q).where.append(where)
    }.map(Question(q.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Question as q).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    name: Option[String] = None,
    answerOne: Option[String] = None,
    answerTwo: Option[String] = None,
    answerThree: Option[String] = None,
    answerFour: Option[String] = None,
    correct: Option[Int] = None,
    newsId: Int)(implicit session: DBSession = autoSession): Question = {
    val generatedKey = withSQL {
      insert.into(Question).namedValues(
        column.name -> name,
        column.answerOne -> answerOne,
        column.answerTwo -> answerTwo,
        column.answerThree -> answerThree,
        column.answerFour -> answerFour,
        column.correct -> correct,
        column.newsId -> newsId
      )
    }.updateAndReturnGeneratedKey.apply()

    Question(
      questionId = generatedKey.toInt,
      name = name,
      answerOne = answerOne,
      answerTwo = answerTwo,
      answerThree = answerThree,
      answerFour = answerFour,
      correct = correct,
      newsId = newsId)
  }

  def batchInsert(entities: Seq[Question])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'name -> entity.name,
        'answerOne -> entity.answerOne,
        'answerTwo -> entity.answerTwo,
        'answerThree -> entity.answerThree,
        'answerFour -> entity.answerFour,
        'correct -> entity.correct,
        'newsId -> entity.newsId))
    SQL("""insert into question(
      name,
      answer_one,
      answer_two,
      answer_three,
      answer_four,
      correct,
      news_id
    ) values (
      {name},
      {answerOne},
      {answerTwo},
      {answerThree},
      {answerFour},
      {correct},
      {newsId}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Question)(implicit session: DBSession = autoSession): Question = {
    withSQL {
      update(Question).set(
        column.questionId -> entity.questionId,
        column.name -> entity.name,
        column.answerOne -> entity.answerOne,
        column.answerTwo -> entity.answerTwo,
        column.answerThree -> entity.answerThree,
        column.answerFour -> entity.answerFour,
        column.correct -> entity.correct,
        column.newsId -> entity.newsId
      ).where.eq(column.questionId, entity.questionId)
    }.update.apply()
    entity
  }

  def destroy(entity: Question)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Question).where.eq(column.questionId, entity.questionId) }.update.apply()
  }

}
