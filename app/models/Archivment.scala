package models

import scalikejdbc._

case class Archivment(
  archiId: Int,
  name: String,
  iconPath: Option[String] = None) {

  def save()(implicit session: DBSession = Archivment.autoSession): Archivment = Archivment.save(this)(session)

  def destroy()(implicit session: DBSession = Archivment.autoSession): Int = Archivment.destroy(this)(session)

}


object Archivment extends SQLSyntaxSupport[Archivment] {

  override val schemaName = Some("public")

  override val tableName = "archivment"

  override val columns = Seq("archi_id", "name", "icon_path")

  def apply(a: SyntaxProvider[Archivment])(rs: WrappedResultSet): Archivment = apply(a.resultName)(rs)
  def apply(a: ResultName[Archivment])(rs: WrappedResultSet): Archivment = new Archivment(
    archiId = rs.get(a.archiId),
    name = rs.get(a.name),
    iconPath = rs.get(a.iconPath)
  )

  val a = Archivment.syntax("a")

  override val autoSession = AutoSession

  def find(archiId: Int)(implicit session: DBSession = autoSession): Option[Archivment] = {
    withSQL {
      select.from(Archivment as a).where.eq(a.archiId, archiId)
    }.map(Archivment(a.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Archivment] = {
    withSQL(select.from(Archivment as a)).map(Archivment(a.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Archivment as a)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Archivment] = {
    withSQL {
      select.from(Archivment as a).where.append(where)
    }.map(Archivment(a.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Archivment] = {
    withSQL {
      select.from(Archivment as a).where.append(where)
    }.map(Archivment(a.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Archivment as a).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    name: String,
    iconPath: Option[String] = None)(implicit session: DBSession = autoSession): Archivment = {
    val generatedKey = withSQL {
      insert.into(Archivment).namedValues(
        column.name -> name,
        column.iconPath -> iconPath
      )
    }.updateAndReturnGeneratedKey.apply()

    Archivment(
      archiId = generatedKey.toInt,
      name = name,
      iconPath = iconPath)
  }

  def batchInsert(entities: Seq[Archivment])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'name -> entity.name,
        'iconPath -> entity.iconPath))
    SQL("""insert into archivment(
      name,
      icon_path
    ) values (
      {name},
      {iconPath}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Archivment)(implicit session: DBSession = autoSession): Archivment = {
    withSQL {
      update(Archivment).set(
        column.archiId -> entity.archiId,
        column.name -> entity.name,
        column.iconPath -> entity.iconPath
      ).where.eq(column.archiId, entity.archiId)
    }.update.apply()
    entity
  }

  def destroy(entity: Archivment)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Archivment).where.eq(column.archiId, entity.archiId) }.update.apply()
  }

}
