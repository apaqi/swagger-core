import models.composition._

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models._
import com.wordnik.swagger.converter._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class CompositionTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "read a model with required params and description" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[Human])

    schemas should serializeToJson (
"""{
  "Human" : {
    "properties" : {
      "name" : {
        "type" : "string"
      },
      "type" : {
        "type" : "string"
      },
      "firstName" : {
        "type" : "string"
      },
      "lastName" : {
        "type" : "string"
      }
    },
    "discriminator" : "type"
  },
  "Pet" : {
    "allOf" : [ {
      "$ref" : "#/definitions/Human"
    }, {
      "required" : [ "isDomestic", "name", "type" ],
      "properties" : {
        "type" : {
          "type" : "string",
          "position" : 1,
          "description" : "The pet type"
        },
        "name" : {
          "type" : "string",
          "position" : 2,
          "description" : "The name of the pet"
        },
        "isDomestic" : {
          "type" : "boolean",
          "position" : 3,
          "default" : false
        }
      }
    } ]
  }
}""")
  }

  it should "read a model with composition" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[Animal])
    schemas should serializeToJson (
"""{
  "Animal" : {
    "properties" : {
      "name" : {
        "type" : "string"
      },
      "type" : {
        "type" : "string"
      }
    },
    "discriminator" : "type"
  },
  "Human" : {
    "allOf" : [ {
      "$ref" : "#/definitions/Animal"
    }, {
      "properties" : {
        "name" : {
          "type" : "string"
        },
        "type" : {
          "type" : "string"
        },
        "firstName" : {
          "type" : "string"
        },
        "lastName" : {
          "type" : "string"
        }
      }
    } ]
  },
  "Pet" : {
    "allOf" : [ {
      "$ref" : "#/definitions/Animal"
    }, {
      "required" : [ "isDomestic", "name", "type" ],
      "properties" : {
        "type" : {
          "type" : "string",
          "position" : 1,
          "description" : "The pet type"
        },
        "name" : {
          "type" : "string",
          "position" : 2,
          "description" : "The name of the pet"
        },
        "isDomestic" : {
          "type" : "boolean",
          "position" : 3,
          "default" : false
        }
      }
    } ]
  }
}""")
  }

  it should "create a model" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[AbstractBaseModelWithoutFields])
    schemas should serializeToJson (
      """{
  "AbstractBaseModelWithoutFields" : {
    "type" : "object",
    "description" : "I am an Abstract Base Model without any declared fields and with Sub-Types"
  },
    "Thing3" : {
      "allOf" : [ {
      "$ref" : "#/definitions/AbstractBaseModelWithoutFields"
      }, {
      "properties" : {
        "a" : {
          "type" : "string",
          "description" : "Additional field a"
        },
        "x" : {
          "type" : "integer",
          "format" : "int32",
          "description" : "Additional field a"
        }
      },
      "description" : "Thing3"
    } ]
  }
}""")
  }
}
