package com.bluecatcode.common.base

import com.bluecatcode.common.base.CustomMatchers._
import com.bluecatcode.common.base.Strings._
import org.junit.runner.RunWith
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FunSuite, _}

@RunWith(classOf[JUnitRunner])
class StringsSuite extends FunSuite with GivenWhenThen with PropertyChecks {

  val nonEmptyAlphaStr: Gen[String] = nonEmptyListOf(alphaChar).map(_.mkString).suchThat(_.forall(_.isLetter))

  test("Should capitalize any alphanumeric string") {
    Given("an alphabetic string")
    forAll(nonEmptyAlphaStr) { (s: String) =>
      When(s"the string is not empty, got: $s")
      val result = capitalize(s)

      Then(s"result's first character is upper case, got: $result")
      result should have(firstCharUpper)
      result should have(theSameTailAs(s))
      result shouldNot be(empty)
    }
  }

  test("Should leave any non-letter and already capitalized starting letter") {
    val samples = Table("string", // header
      "", " ", "\n", "\t", "\r", "0", "中国", "TLA"
    )
    forAll(samples) {
      s => capitalize(s) should be(s)
    }
  }

  test("Should capitalize any first lower case letter") {
    val samples = Table(("string", "expected"), // header
      ("a", "A"),
      ("łąka", "Łąka")
    )
    forAll(samples) {
      (s, e: String) => capitalize(s) should be(e)
    }
  }

  test("Should throw on null argument") {
    Given("null argument")
    val s: String = null

    Then("IllegalArgumentException is thrown")
    intercept[IllegalArgumentException] {
      When("passed to the function")
      capitalize(s)
    }
  }

}

