package models

import scalikejdbc._

case class News(
  newsId: Int,
  title: String,
  href: String,
  news: String,
  category: String) {

  def save()(implicit session: DBSession = News.autoSession): News = News.save(this)(session)

  def destroy()(implicit session: DBSession = News.autoSession): Int = News.destroy(this)(session)

}


object News extends SQLSyntaxSupport[News] {

  override val schemaName = Some("public")

  override val tableName = "news"

  override val columns = Seq("news_id", "title", "href", "news", "category")

  def apply(n: SyntaxProvider[News])(rs: WrappedResultSet): News = apply(n.resultName)(rs)
  def apply(n: ResultName[News])(rs: WrappedResultSet): News = new News(
    newsId = rs.get(n.newsId),
    title = rs.get(n.title),
    href = rs.get(n.href),
    news = rs.get(n.news),
    category = rs.get(n.category)
  )

  val n = News.syntax("n")

  override val autoSession = AutoSession

  def find(newsId: Int)(implicit session: DBSession = autoSession): Option[News] = {
    withSQL {
      select.from(News as n).where.eq(n.newsId, newsId)
    }.map(News(n.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[News] = {
    withSQL(select.from(News as n)).map(News(n.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(News as n)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[News] = {
    withSQL {
      select.from(News as n).where.append(where)
    }.map(News(n.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[News] = {
    withSQL {
      select.from(News as n).where.append(where)
    }.map(News(n.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(News as n).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    title: String,
    href: String,
    news: String,
    category: String)(implicit session: DBSession = autoSession): News = {
    val generatedKey = withSQL {
      insert.into(News).namedValues(
        column.title -> title,
        column.href -> href,
        column.news -> news,
        column.category -> category
      )
    }.updateAndReturnGeneratedKey.apply()

    News(
      newsId = generatedKey.toInt,
      title = title,
      href = href,
      news = news,
      category = category)
  }

  def batchInsert(entities: Seq[News])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'title -> entity.title,
        'href -> entity.href,
        'news -> entity.news,
        'category -> entity.category))
    SQL("""insert into news(
      title,
      href,
      news,
      category
    ) values (
      {title},
      {href},
      {news},
      {category}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: News)(implicit session: DBSession = autoSession): News = {
    withSQL {
      update(News).set(
        column.newsId -> entity.newsId,
        column.title -> entity.title,
        column.href -> entity.href,
        column.news -> entity.news,
        column.category -> entity.category
      ).where.eq(column.newsId, entity.newsId)
    }.update.apply()
    entity
  }

  def destroy(entity: News)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(News).where.eq(column.newsId, entity.newsId) }.update.apply()
  }

}
