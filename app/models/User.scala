package models

import scalikejdbc._

case class User(
  userId: Int,
  email: String,
  password: String,
  name: String,
  role: String) {

  def save()(implicit session: DBSession = User.autoSession): User = User.save(this)(session)

  def destroy()(implicit session: DBSession = User.autoSession): Int = User.destroy(this)(session)

}


object User extends SQLSyntaxSupport[User] {

  override val schemaName = Some("public")

  override val tableName = "user"

  override val columns = Seq("user_id", "email", "password", "name", "role")

  def apply(u: SyntaxProvider[User])(rs: WrappedResultSet): User = apply(u.resultName)(rs)
  def apply(u: ResultName[User])(rs: WrappedResultSet): User = new User(
    userId = rs.get(u.userId),
    email = rs.get(u.email),
    password = rs.get(u.password),
    name = rs.get(u.name),
    role = rs.get(u.role)
  )

  val u = User.syntax("u")

  override val autoSession = AutoSession

  def find(userId: Int)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.eq(u.userId, userId)
    }.map(User(u.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[User] = {
    withSQL(select.from(User as u)).map(User(u.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(User as u)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.append(where)
    }.map(User(u.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[User] = {
    withSQL {
      select.from(User as u).where.append(where)
    }.map(User(u.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(User as u).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    email: String,
    password: String,
    name: String,
    role: String)(implicit session: DBSession = autoSession): User = {
    val generatedKey = withSQL {
      insert.into(User).namedValues(
        column.email -> email,
        column.password -> password,
        column.name -> name,
        column.role -> role
      )
    }.updateAndReturnGeneratedKey.apply()

    User(
      userId = generatedKey.toInt,
      email = email,
      password = password,
      name = name,
      role = role)
  }

  def batchInsert(entities: Seq[User])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'email -> entity.email,
        'password -> entity.password,
        'name -> entity.name,
        'role -> entity.role))
    SQL("""insert into user(
      email,
      password,
      name,
      role
    ) values (
      {email},
      {password},
      {name},
      {role}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: User)(implicit session: DBSession = autoSession): User = {
    withSQL {
      update(User).set(
        column.userId -> entity.userId,
        column.email -> entity.email,
        column.password -> entity.password,
        column.name -> entity.name,
        column.role -> entity.role
      ).where.eq(column.userId, entity.userId)
    }.update.apply()
    entity
  }

  def destroy(entity: User)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(User).where.eq(column.userId, entity.userId) }.update.apply()
  }

}
