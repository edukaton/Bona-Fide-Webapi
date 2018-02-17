package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class ArchivmentSpec extends Specification {

  "Archivment" should {

    val a = Archivment.syntax("a")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Archivment.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Archivment.findBy(sqls.eq(a.archiId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Archivment.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Archivment.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Archivment.findAllBy(sqls.eq(a.archiId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Archivment.countBy(sqls.eq(a.archiId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Archivment.create(name = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Archivment.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Archivment.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Archivment.findAll().head
      val deleted = Archivment.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Archivment.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Archivment.findAll()
      entities.foreach(e => Archivment.destroy(e))
      val batchInserted = Archivment.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
