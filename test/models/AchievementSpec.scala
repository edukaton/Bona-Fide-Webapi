package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class AchievementSpec extends Specification {

  "Achievement" should {

    val a = Achievement.syntax("a")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Achievement.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Achievement.findBy(sqls.eq(a.achieId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Achievement.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Achievement.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Achievement.findAllBy(sqls.eq(a.achieId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Achievement.countBy(sqls.eq(a.achieId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Achievement.create(name = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Achievement.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Achievement.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Achievement.findAll().head
      val deleted = Achievement.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Achievement.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Achievement.findAll()
      entities.foreach(e => Achievement.destroy(e))
      val batchInserted = Achievement.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
