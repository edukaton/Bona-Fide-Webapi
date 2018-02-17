package models

import scalikejdbc._

case class Rank(
  rankId: Int,
  score: Option[Double] = None) {

  def save()(implicit session: DBSession = Rank.autoSession): Rank = Rank.save(this)(session)

  def destroy()(implicit session: DBSession = Rank.autoSession): Int = Rank.destroy(this)(session)

}


object Rank extends SQLSyntaxSupport[Rank] {

  override val schemaName = Some("public")

  override val tableName = "rank"

  override val columns = Seq("rank_id", "score")

  def apply(r: SyntaxProvider[Rank])(rs: WrappedResultSet): Rank = apply(r.resultName)(rs)
  def apply(r: ResultName[Rank])(rs: WrappedResultSet): Rank = new Rank(
    rankId = rs.get(r.rankId),
    score = rs.get(r.score)
  )

  val r = Rank.syntax("r")

  override val autoSession = AutoSession

  def find(rankId: Int)(implicit session: DBSession = autoSession): Option[Rank] = {
    withSQL {
      select.from(Rank as r).where.eq(r.rankId, rankId)
    }.map(Rank(r.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Rank] = {
    withSQL(select.from(Rank as r)).map(Rank(r.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Rank as r)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Rank] = {
    withSQL {
      select.from(Rank as r).where.append(where)
    }.map(Rank(r.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Rank] = {
    withSQL {
      select.from(Rank as r).where.append(where)
    }.map(Rank(r.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Rank as r).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    score: Option[Double] = None)(implicit session: DBSession = autoSession): Rank = {
    val generatedKey = withSQL {
      insert.into(Rank).namedValues(
        column.score -> score
      )
    }.updateAndReturnGeneratedKey.apply()

    Rank(
      rankId = generatedKey.toInt,
      score = score)
  }

  def batchInsert(entities: Seq[Rank])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'score -> entity.score))
    SQL("""insert into rank(
      score
    ) values (
      {score}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Rank)(implicit session: DBSession = autoSession): Rank = {
    withSQL {
      update(Rank).set(
        column.rankId -> entity.rankId,
        column.score -> entity.score
      ).where.eq(column.rankId, entity.rankId)
    }.update.apply()
    entity
  }

  def destroy(entity: Rank)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Rank).where.eq(column.rankId, entity.rankId) }.update.apply()
  }

}
