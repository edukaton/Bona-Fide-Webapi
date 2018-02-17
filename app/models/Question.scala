package models

import scalikejdbc._

case class Question(
  questionId: Int,
  name: Option[String] = None,
  answer1: Option[String] = None,
  answer2: Option[String] = None,
  answer3: Option[String] = None,
  answer4: Option[String] = None,
  correct: Option[Int] = None,
  newsId: Int) {

  def save()(implicit session: DBSession = Question.autoSession): Question = Question.save(this)(session)

  def destroy()(implicit session: DBSession = Question.autoSession): Int = Question.destroy(this)(session)

}


object Question extends SQLSyntaxSupport[Question] {

  override val schemaName = Some("public")

  override val tableName = "question"

  override val columns = Seq("question_id", "name", "answer_1", "answer_2", "answer_3", "answer_4", "correct", "news_id")

  def apply(q: SyntaxProvider[Question])(rs: WrappedResultSet): Question = apply(q.resultName)(rs)
  def apply(q: ResultName[Question])(rs: WrappedResultSet): Question = new Question(
    questionId = rs.get(q.questionId),
    name = rs.get(q.name),
    answer1 = rs.get(q.answer1),
    answer2 = rs.get(q.answer2),
    answer3 = rs.get(q.answer3),
    answer4 = rs.get(q.answer4),
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
    answer1: Option[String] = None,
    answer2: Option[String] = None,
    answer3: Option[String] = None,
    answer4: Option[String] = None,
    correct: Option[Int] = None,
    newsId: Int)(implicit session: DBSession = autoSession): Question = {
    val generatedKey = withSQL {
      insert.into(Question).namedValues(
        column.name -> name,
        column.answer1 -> answer1,
        column.answer2 -> answer2,
        column.answer3 -> answer3,
        column.answer4 -> answer4,
        column.correct -> correct,
        column.newsId -> newsId
      )
    }.updateAndReturnGeneratedKey.apply()

    Question(
      questionId = generatedKey.toInt,
      name = name,
      answer1 = answer1,
      answer2 = answer2,
      answer3 = answer3,
      answer4 = answer4,
      correct = correct,
      newsId = newsId)
  }

  def batchInsert(entities: Seq[Question])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'name -> entity.name,
        'answer1 -> entity.answer1,
        'answer2 -> entity.answer2,
        'answer3 -> entity.answer3,
        'answer4 -> entity.answer4,
        'correct -> entity.correct,
        'newsId -> entity.newsId))
    SQL("""insert into question(
      name,
      answer_1,
      answer_2,
      answer_3,
      answer_4,
      correct,
      news_id
    ) values (
      {name},
      {answer1},
      {answer2},
      {answer3},
      {answer4},
      {correct},
      {newsId}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Question)(implicit session: DBSession = autoSession): Question = {
    withSQL {
      update(Question).set(
        column.questionId -> entity.questionId,
        column.name -> entity.name,
        column.answer1 -> entity.answer1,
        column.answer2 -> entity.answer2,
        column.answer3 -> entity.answer3,
        column.answer4 -> entity.answer4,
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
