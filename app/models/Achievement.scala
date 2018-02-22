package models

import scalikejdbc._

case class Achievement(
  achieId: Int,
  name: String,
  iconpath: Option[String] = None) {

  def save()(implicit session: DBSession = Achievement.autoSession): Achievement = Achievement.save(this)(session)

  def destroy()(implicit session: DBSession = Achievement.autoSession): Int = Achievement.destroy(this)(session)

}


object Achievement extends SQLSyntaxSupport[Achievement] {

  override val schemaName = Some("public")

  override val tableName = "achievement"

  override val columns = Seq("achie_id", "name", "iconpath")

  def apply(a: SyntaxProvider[Achievement])(rs: WrappedResultSet): Achievement = apply(a.resultName)(rs)
  def apply(a: ResultName[Achievement])(rs: WrappedResultSet): Achievement = new Achievement(
    achieId = rs.get(a.achieId),
    name = rs.get(a.name),
    iconpath = rs.get(a.iconpath)
  )

  val a = Achievement.syntax("a")

  override val autoSession = AutoSession

  def find(achieId: Int)(implicit session: DBSession = autoSession): Option[Achievement] = {
    withSQL {
      select.from(Achievement as a).where.eq(a.achieId, achieId)
    }.map(Achievement(a.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Achievement] = {
    withSQL(select.from(Achievement as a)).map(Achievement(a.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Achievement as a)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Achievement] = {
    withSQL {
      select.from(Achievement as a).where.append(where)
    }.map(Achievement(a.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Achievement] = {
    withSQL {
      select.from(Achievement as a).where.append(where)
    }.map(Achievement(a.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Achievement as a).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    name: String,
    iconpath: Option[String] = None)(implicit session: DBSession = autoSession): Achievement = {
    val generatedKey = withSQL {
      insert.into(Achievement).namedValues(
        column.name -> name,
        column.iconpath -> iconpath
      )
    }.updateAndReturnGeneratedKey.apply()

    Achievement(
      achieId = generatedKey.toInt,
      name = name,
      iconpath = iconpath)
  }

  def batchInsert(entities: Seq[Achievement])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'name -> entity.name,
        'iconpath -> entity.iconpath))
    SQL("""insert into achievement(
      name,
      iconpath
    ) values (
      {name},
      {iconpath}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Achievement)(implicit session: DBSession = autoSession): Achievement = {
    withSQL {
      update(Achievement).set(
        column.achieId -> entity.achieId,
        column.name -> entity.name,
        column.iconpath -> entity.iconpath
      ).where.eq(column.achieId, entity.achieId)
    }.update.apply()
    entity
  }

  def destroy(entity: Achievement)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Achievement).where.eq(column.achieId, entity.achieId) }.update.apply()
  }

}
