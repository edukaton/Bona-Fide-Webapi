package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class RankSpec extends Specification {

  "Rank" should {

    val r = Rank.syntax("r")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Rank.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Rank.findBy(sqls.eq(r.rankId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Rank.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Rank.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Rank.findAllBy(sqls.eq(r.rankId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Rank.countBy(sqls.eq(r.rankId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Rank.create()
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Rank.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Rank.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Rank.findAll().head
      val deleted = Rank.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Rank.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Rank.findAll()
      entities.foreach(e => Rank.destroy(e))
      val batchInserted = Rank.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
