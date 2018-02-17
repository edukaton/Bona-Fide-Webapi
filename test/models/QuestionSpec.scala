package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class QuestionSpec extends Specification {

  "Question" should {

    val q = Question.syntax("q")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Question.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Question.findBy(sqls.eq(q.questionId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Question.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Question.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Question.findAllBy(sqls.eq(q.questionId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Question.countBy(sqls.eq(q.questionId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Question.create(newsId = 123)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Question.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Question.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Question.findAll().head
      val deleted = Question.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Question.find(123)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Question.findAll()
      entities.foreach(e => Question.destroy(e))
      val batchInserted = Question.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
