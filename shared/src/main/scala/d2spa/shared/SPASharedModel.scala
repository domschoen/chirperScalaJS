package d2spa.shared

import boopickle.Default._
import boopickle.{MaterializePicklerFallback, TransformPicklers}
import d2spa.shared.WebSocketMessages._




object WebSocketMessages {

  // Client ---> Server
  sealed trait WebSocketMsgIn
  final case class StringMsgIn(string: String) extends WebSocketMsgIn
  final case object GetDebugConfiguration extends WebSocketMsgIn
  final case object FetchEOModel extends WebSocketMsgIn
  final case object FetchMenus extends WebSocketMsgIn
  final case class GetMetaData(d2wContext: D2WContextFullFledged) extends WebSocketMsgIn
  final case class RuleToFire(rhs: FiringD2WContext, key: String) extends WebSocketMsgIn
  final case class DeleteEOMsgIn(eo: EO) extends WebSocketMsgIn
  final case class CompleteEO(eo: EOFault, missingKeys: Set[String]) extends WebSocketMsgIn
  final case class HydrateEOs(entityName: String, pks: Seq[EOPk], missingKeys: Set[String]) extends WebSocketMsgIn
  final case class SearchAll(fs: EOFetchAll) extends WebSocketMsgIn
  final case class Search(fs: EOQualifiedFetch) extends WebSocketMsgIn
  final case class NewEO(entityName: String, eo: EO) extends WebSocketMsgIn
  final case class UpdateEO(eo: EO) extends WebSocketMsgIn

  // Server ---> Client
  sealed trait WebSocketMsgOut

  final case class DebugConfMsg(showD2WDebugButton: Boolean) extends WebSocketMsgOut
  final case class FetchedEOModel(eomodel: EOModel) extends WebSocketMsgOut
  final case class FetchedMenus(menus: Menus) extends WebSocketMsgOut
  final case class RuleResults(ruleResults: List[RuleResult]) extends WebSocketMsgOut
  final case class FetchedObjectsMsgOut(eos: Seq[EO]) extends WebSocketMsgOut
  final case class FetchedObjectsForListMsgOut(entityName: String, eos: Seq[EO]) extends WebSocketMsgOut
  final case class SavingResponseMsgOut(eo: EO) extends WebSocketMsgOut
  final case class DeletingResponseMsgOut(eo: EO) extends WebSocketMsgOut
}

//class RuleResults(ruleResults: List[RuleResult])



object TaskDefine {
  val edit = "edit"
  val inspect = "inspect"
  val list = "list"
  val query = "query"
}

object RuleKeys {
  val keyWhenRelationship = "keyWhenRelationship"
  val displayNameForKeyWhenRelationship = "displayNameForKeyWhenRelationship"
  val displayNameForProperty = "displayNameForProperty"
  val displayNameForEntity = "displayNameForEntity"
  val componentName = "componentName"
  val displayPropertyKeys = "displayPropertyKeys"
  val destinationEos = "destinationEos"
  val pkAttributeName = "pkAttributeName"
  val listConfigurationName = "listConfigurationName"
  val inspectConfigurationName = "inspectConfigurationName"
  val pageConfiguration = "pageConfiguration"
  val propertyType = "propertyType"
  val destinationEntity = "destinationEntity"
  val isInspectAllowed = "isInspectAllowed"
  val isEditAllowed = "isEditAllowed"
  val isDeleteAllowed = "isDeleteAllowed"
}




//case class DateValue(value: java.util.Date) extends EOValue

//case class EO(entityName: String, values: Map[String, EOValue] = Map.empty[String, EOValue], pk: EOPk, validationError: Option[String] = None)
case class EO(entityName: String, keys: List[String] = List.empty[String], values: List[EOValue] = List.empty[EOValue], pk: EOPk, validationError: Option[String] = None)
//case class EO2(entityName: String, values: List[EOValue2], pk: List[Int], validationError: Option[String] = None)
//case class EO2(entityName: String, values: Map[String,EOValue2] = Map(),  pk: List[Int])
//case class EOValueMap(values: Map[String,String] = Map())

case class EOPack(eos: List[EO])

object Test3 extends MaterializePicklerFallback {

  import boopickle.Default._
  implicit val eoPicker: Pickler[EO] = generatePickler[EO]


  implicit val eoValuePickler = compositePickler[EOValue]

  implicit val stringValuePickler: Pickler[StringValue] = generatePickler[StringValue]
  implicit val intValuePickler: Pickler[IntValue] = generatePickler[IntValue]
  implicit val booleanValuePickler: Pickler[BooleanValue] = generatePickler[BooleanValue]
  implicit val objectValuePicker: Pickler[ObjectValue] = generatePickler[ObjectValue]
  implicit val objectsValuePicker: Pickler[ObjectsValue] = generatePickler[ObjectsValue]

  eoValuePickler.addConcreteType[StringValue].addConcreteType[IntValue].addConcreteType[BooleanValue].addConcreteType[ObjectValue].addConcreteType[ObjectsValue]

  def serializer(c: EO) = Pickle.intoBytes(c)
}




object Test8 extends MaterializePicklerFallback {
  import boopickle.Default._
  implicit val bPickler = generatePickler[EO]
  def serializer(c: EOPack) = Pickle.intoBytes(c)
}

// For EOValue and Class extending
object Test5 extends MaterializePicklerFallback {

  import boopickle.Default._
  implicit val eoPicker: Pickler[EO] = generatePickler[EO]


  implicit val eoValuePickler = compositePickler[EOValue]

  implicit val stringValuePickler: Pickler[StringValue] = generatePickler[StringValue]
  implicit val intValuePickler: Pickler[IntValue] = generatePickler[IntValue]
  implicit val booleanValuePickler: Pickler[BooleanValue] = generatePickler[BooleanValue]
  implicit val objectValuePicker: Pickler[ObjectValue] = generatePickler[ObjectValue]
  implicit val objectsValuePicker: Pickler[ObjectsValue] = generatePickler[ObjectsValue]

  eoValuePickler.addConcreteType[StringValue].addConcreteType[IntValue].addConcreteType[BooleanValue].addConcreteType[ObjectValue].addConcreteType[ObjectsValue]

  def serializer(c: EOValue) = Pickle.intoBytes(c)
}




/*object Test12 extends MaterializePicklerFallback {

  import boopickle.Default._
  implicit val eoPicker: Pickler[EO2] = generatePickler[EO2]


  implicit val eoValuePickler = compositePickler[EOValue2]

  implicit val stringValuePickler: Pickler[StringValue2] = generatePickler[StringValue2]
  implicit val intValuePickler: Pickler[IntValue2] = generatePickler[IntValue2]
  implicit val booleanValuePickler: Pickler[BooleanValue2] = generatePickler[BooleanValue2]
  implicit val objectValuePicker: Pickler[ObjectValue2] = generatePickler[ObjectValue2]
  implicit val objectsValuePicker: Pickler[ObjectsValue2] = generatePickler[ObjectsValue2]

  eoValuePickler.addConcreteType[StringValue2].addConcreteType[IntValue2].addConcreteType[BooleanValue2].addConcreteType[ObjectValue2].addConcreteType[ObjectsValue2]

  def serializer(c: EOValue2) = Pickle.intoBytes(c)
}*/



object Test4 extends MaterializePicklerFallback {

  import boopickle.Default._
  implicit val eoFetchSpecifactionPicker: Pickler[EOFetchSpecification] = generatePickler[EOFetchSpecification]

  implicit val eoPicker: Pickler[EO] = generatePickler[EO]

  implicit val qualifierPickler = compositePickler[EOQualifier]

  implicit val eoAndQualifierPicker: Pickler[EOAndQualifier] = generatePickler[EOAndQualifier]
  implicit val eoOrQualifierPicker: Pickler[EOOrQualifier] = generatePickler[EOOrQualifier]
  implicit val eoKeyValueQualifierPicker: Pickler[EOKeyValueQualifier] = generatePickler[EOKeyValueQualifier]
  implicit val eoNotQualifierPicker: Pickler[EONotQualifier] = generatePickler[EONotQualifier]

  qualifierPickler.addConcreteType[EOAndQualifier].addConcreteType[EOOrQualifier].addConcreteType[EOKeyValueQualifier].addConcreteType[EONotQualifier]

  def serializer(c: EOQualifier) = Pickle.intoBytes(c)

}


object Test10 extends MaterializePicklerFallback {
  import boopickle.Default._

  implicit val eoPkPicker: Pickler[EOPk] = generatePickler[EOPk]
  implicit val eoPicker: Pickler[EO] = generatePickler[EO]
  implicit val eoValuePicker: Pickler[EOValue] = generatePickler[EOValue]
  //implicit val eo2Picker: Pickler[EO2] = generatePickler[EO2]
  implicit val eoPackPicker: Pickler[EOPack] = generatePickler[EOPack]



  implicit val webSocketMsgOutPickler = compositePickler[WebSocketMsgOut]

  implicit val debugConfMsgPicker: Pickler[DebugConfMsg] = generatePickler[DebugConfMsg]
  implicit val fetchedEOModelPicker: Pickler[FetchedEOModel] = generatePickler[FetchedEOModel]
  implicit val fetchedMenusPicker: Pickler[FetchedMenus] = generatePickler[FetchedMenus]
  implicit val ruleResultsPicker: Pickler[RuleResults] = generatePickler[RuleResults]
  implicit val fetchedObjectsPicker: Pickler[FetchedObjectsMsgOut] = generatePickler[FetchedObjectsMsgOut]
  implicit val savingResponseMsgOutPicker: Pickler[SavingResponseMsgOut] = generatePickler[SavingResponseMsgOut]
  implicit val deletingResponseMsgOutPicker: Pickler[DeletingResponseMsgOut] = generatePickler[DeletingResponseMsgOut]

  webSocketMsgOutPickler.addConcreteType[DebugConfMsg]
    .addConcreteType[FetchedEOModel]
    .addConcreteType[FetchedMenus]
    .addConcreteType[RuleResults]
    .addConcreteType[FetchedObjectsMsgOut]
    .addConcreteType[SavingResponseMsgOut]
    .addConcreteType[DeletingResponseMsgOut]


  def serializer(c: WebSocketMsgOut) = Pickle.intoBytes(c)
}


sealed trait EOValue
case class StringValue(value: String) extends EOValue
case class IntValue(value : Int) extends EOValue
case class BooleanValue(value : Boolean) extends EOValue
case class ObjectValue(eo: EO) extends EOValue
case class ObjectsValue(eos: List[EOPk]) extends EOValue
case object EmptyValue extends  EOValue
//case object NoneValue extends EOValue

/*sealed trait EOValue2
case class StringValue2(value: String) extends EOValue2
case class IntValue2(value : Int) extends EOValue2
case class BooleanValue2(value : Boolean) extends EOValue2
case class ObjectValue2(eo: EO2) extends EOValue2
case class ObjectsValue2(eos: List[EOPk]) extends EOValue2
case object EmptyValue2 extends  EOValue2*/


case class EOPk(pks: List[Int])

object EOValue {


  def isNew(pk: EOPk) = pk.pks.size == 1 && pk.pks.head < 0

  // When saving an EO, we have to remove any non db attributes
  def purgedEO(eo: EO) = {
    eo
  }

  def size(value: EOValue) = value match {
    case StringValue(s) => s.length
    case IntValue(i) => i.toString.length
    case BooleanValue(value) => 1
    case ObjectValue(eo) => 0
    case ObjectsValue(eos: Seq[EOPk]) => eos.size
    case EmptyValue => 0
  }

  def objectValue(eoOpt: Option[EO]) = {
    eoOpt match {
      case Some(eo) => ObjectValue(eo = eo)
      case None => EmptyValue
    }
  }

  def stringV(value: String) = StringValue(value)
  def intV(value: Int) = IntValue(value)
  def eoV(value: EO) =  ObjectValue(eo = value)
  def eosV(value: List[EOPk]) = ObjectsValue(eos = value)


  def eoValueWithString(str: String) = if (str.length == 0) EmptyValue else StringValue(str)
  def eoValueWithInt(str: String) = if (str.length == 0) EmptyValue else IntValue(str.toInt)

  def createAndInsertNewObject(insertedEOs: Map[String, Map[EOPk, EO]], entityName: String): (Map[String, Map[EOPk, EO]], EO) = {
    //val entityName = entity.name
    val insertedEOsForEntityOpt = if (insertedEOs.contains(entityName)) Some(insertedEOs(entityName)) else None
    insertedEOsForEntityOpt match {
      // We have alreaydy existing in memory eo for that entityName
      case Some(insertedEOsForEntity) =>
        val existingPks = insertedEOsForEntity.keySet.map(_.pks.head)
        val newMemID = if (existingPks.isEmpty) -1 else existingPks.min - 1

        val newPk = EOPk(List(newMemID))
        val newEO = EO(entityName, List.empty[String], List.empty[EOValue], pk = newPk)
        val newEntityMap = insertedEOsForEntity + (newPk -> newEO)
        val newInsertedEOs = insertedEOs + (entityName -> newEntityMap)
        (newInsertedEOs, newEO)
      case None =>
        val newPk = EOPk(List(-1))
        val newEO = EO(entityName, List.empty[String], List.empty[EOValue], pk = newPk)
        val newEntityMap = Map(newPk -> newEO)
        val newInsertedEOs = Map(entityName -> newEntityMap)
        (newInsertedEOs, newEO)
    }
  }

  //def createAndInsertNewObject(insertedEOs: Map[String, Map[List[Int], EO]], eomodel: EOModel, entityName: String): (Map[String, Map[List[Int], EO]], EO) = {
  //    val entity = EOModelUtils.entityNamed(eomodel, entityName).get
  //    createAndInsertNewObject(insertedEOs,entity)
  //}


  // For creation of objects only ?
  def dryEOWithEntity(entityName: String, pk: EOPk) = {
    //val pkAttributeNames = entity.pkAttributeNames
    //val valuesMap = pkAttributeNames.zip(pk.map(IntValue(_))).toMap
    EO(entityName, List.empty[String], List.empty[EOValue], pk)
  }


  def juiceString(value: EOValue): String =
    value match {
      case StringValue(value) => if (value == null) "" else value
      case IntValue(value) => value.toString
      case BooleanValue(value) => value.toString
      case ObjectValue(eo) => eo.toString
      case EmptyValue => ""
      case _ => ""
    }


  def juiceInt(v: EOValue): Int =
    v match {
      case StringValue(value) => 0
      case IntValue(value) => value
      case ObjectValue(eo) => 0
      case _ => 0
    }

  def juiceBoolean(v: EOValue): Boolean =
    v match {
      case BooleanValue(value) => value
      case StringValue(value) => value.equals("true")
      case IntValue(value) => value == 1
      case ObjectValue(eo) => false
      case _ => false
    }

  def isDefined(value: EOValue): Boolean =
    value match {
      case EmptyValue => false
      case _ => true
    }


  def definedValues(eo: EO): Map[String,EOValue] = {
    val valueMap = keyValues(eo)
    valueMap filter (v => {
      EOValue.isDefined(v._2)
    })
  }

  def keyValues(eo: EO) = {
    eo.keys.zip(eo.values).toMap
  }

  def stringValueForKey(eo: EO, key: String) = {
    valueForKey(eo, key) match {
      case Some(value) => juiceString(value)
      case None => ""
    }
  }


  def valueForKey(eo: EO, key: String) = {
    val valueMap = keyValues(eo)
    if (valueMap.contains(key)) {
      Some(valueMap(key))
    } else
      None
  }

  def takeValueForKey(eo: EO, eovalue: EOValue, key: String): EO = {
    val valueMap = keyValues(eo)
    val newValueMap = (valueMap - key) + (key -> eovalue)
    takeValuesForKeys(eo, newValueMap)
  }

  def takeValuesForKeys(eo: EO, keyValuePairs: Map[String,EOValue]): EO = {
    eo.copy(keys = keyValuePairs.keys.toList, values = keyValuePairs.values.toList)
  }

  // case class EO(entity: EOEntity, values: Map[String,EOValue], validationError: Option[String])
  def completeEoWithEo(existingEO: EO, refreshedEO: EO): EO = {
    val existingValues = keyValues(existingEO)
    val refreshedValues = keyValues(refreshedEO)
    val newValues = existingValues ++ refreshedValues
    existingEO.copy(keys = newValues.keys.toList, values = newValues.values.toList, validationError = refreshedEO.validationError)
  }

  def isNew(eo: EO) = {
    eo.pk.pks.head < 0
    //val pk = EOValue.pk(eo)
    //(pk.isDefined && pk.get < 0) || pk.isEmpty
    //eo.memID.isDefined
  }

  // For single pk EO
  def pk(eoModel: EOModel, eo: EO): Option[EOPk] = {
    val entityName = eo.entityName
    val entityOpt = EOModelUtils.entityNamed(eoModel,entityName)
    entityOpt match {
      case Some(entity) =>
        val pkAttributeNames = entity.pkAttributeNames
        if (eo.keys.contains(pkAttributeNames.head)) {
          Some(EOPk(pkAttributeNames.map(pkAttributeName => EOValue.juiceInt(valueForKey(eo,pkAttributeName).get))))
        } else None
      case None => None
    }
  }

  def refaultEO(eo: EO) = {
    EOFault(eo.entityName, eo.pk)
  }

}







sealed trait EOFetchSpecification

case class EOFetchAll(entityName: String, sortOrderings: List[EOSortOrdering] = List()) extends EOFetchSpecification
case class EOQualifiedFetch(entityName: String, qualifier: EOQualifier, sortOrderings: List[EOSortOrdering] = List()) extends EOFetchSpecification


object EOFetchSpecification {
  def objectsWithFetchSpecification(eos: List[EO], fs: EOFetchSpecification): List[EO] = {

    // TODO sort orderings
    fs match {
      case fa: EOFetchAll => eos
      case fq: EOQualifiedFetch =>
        //println("filter with Qualifier " + fq.qualifier)
        EOQualifier.filteredEOsWithQualifier(eos, fq.qualifier)
    }
  }

  def entityName(fs: EOFetchSpecification): String = {
    fs match {
      case fa: EOFetchAll => fa.entityName
      case fq: EOQualifiedFetch => fq.entityName
    }

  }

}

object EOQualifier {

  def filteredEOsWithQualifier(eos: List[EO], qualifier: EOQualifier) = {
    eos.filter(eo => evaluateWithEO(eo, qualifier))
  }

  def evaluateWithEO(eo: EO, qualifier: EOQualifier): Boolean = {
    //println("evaluateWithEO " + eo + " q " + qualifier)
    qualifier match {
      case EOAndQualifier(qualifiers) =>
        val headQ = qualifiers.head
        val remaining = qualifiers.tail
        val headQValue = evaluateWithEO(eo, headQ)
        if (!headQValue) {
          false
        } else {
          if (remaining.isEmpty) true else evaluateWithEO(eo, EOAndQualifier(remaining))
        }

      case EOOrQualifier(qualifiers) =>
        val headQ = qualifiers.head
        val remaining = qualifiers.tail
        if (evaluateWithEO(eo, headQ)) {
          true
        } else {
          if (remaining.isEmpty) false else evaluateWithEO(eo, EOOrQualifier(remaining))
        }
      case EOKeyValueQualifier(key, selector, value) =>
        val eoValueOpt = EOValue.valueForKey(eo, key)

        eoValueOpt match {
            // eo has a value which is not empty
          case Some(eoValue) =>
            //println("Compare " + eoValue + " with " + value)
            // TODO check the value
            eoValue match {
              case StringValue(str) =>
                // Qualifier value hopefully of the same type or empty
                value match {
                  case StringValue(qualStr) =>
                        val lStr = str.toLowerCase
                        val lQualStr = qualStr.toLowerCase
                        lStr.indexOf(lQualStr) >= 0
                  case EmptyValue => false
                    // comparing apple with banana -> more like an error
                  case _ => false
                }
              case EmptyValue => value match {
                case EmptyValue => true
                case _ => false
              }
              case BooleanValue(bv)  =>
                // Qualifier value hopefully of the same type or empty
                value match {
                  case BooleanValue(qualBv) =>
                    bv == qualBv
                  case EmptyValue => false
                  // comparing apple with banana -> more like an error
                  case _ => false
                }

                /// For all other type, we fall back the following default behaviour:
              case _ => eoValue.equals(value)
            }
          // eo has no value defined for that key
          case None =>
            println("Error: try to compare no fetched or non existing value for key " + key + " for entity " + eo.entityName)
            false
        }


      case EONotQualifier(qualifier) =>
        !evaluateWithEO(eo, qualifier)
    }
  }
}

case class FrontendRequest(text: String)
case class FrontendResponse(value: Int)

sealed trait EOQualifier

case class EOAndQualifier(qualifiers : List[EOQualifier]) extends EOQualifier
case class EOOrQualifier(qualifiers : List[EOQualifier]) extends EOQualifier
case class EOKeyValueQualifier(key: String, selector : String, value: EOValue) extends EOQualifier
case class EONotQualifier(qualifier: EOQualifier) extends EOQualifier

case class EOSortOrdering(key: String, selector: String)

case class EOModel(entities: List[EOEntity])
case class EOEntity(name: String, pkAttributeNames: List[String] = List(), attributes: List[String] = List(), relationships: List[EORelationship] = List())
case class EORelationship(sourceAttributeName: List[String], name: String, destinationEntityName: String)

//case class EORef(entityName: String, id: Int)

case class Menus(menus: List[MainMenu]) //, showDebugButton: Boolean)
case class MainMenu(id: Int, title: String,  children: List[Menu])
case class Menu(id:Int, title: String, entity: EOEntity)

case class EOFault(entityName : String, pk: EOPk)

//case class PreviousTask(task: String, pk: Option[Int])


case class PotFiringKey (value: Either[RuleToFire, Option[String]])

case class FiringD2WContext(entityName: Option[String],
                            task: Option[String],
                            propertyKey:  Option[String] = None,
                            pageConfiguration: PotFiringKey = PotFiringKey(Right(None))
                           )



case class D2WContextFullFledged(entityName: Option[String],
                  task: Option[String],
                  propertyKey:  Option[String] = None,
                  pageConfiguration: Option[String] = None)

case class RuleResult(rhs: D2WContextFullFledged, key: String, value: RuleValue)
case class RuleValue(stringV: Option[String] = None, stringsV: List[String] = List())

object RulesUtilities {

  def convertFullFledgedToFiringD2WContext(d2wContext: D2WContextFullFledged): FiringD2WContext = {
    FiringD2WContext(
    d2wContext.entityName,
    d2wContext.task,
    d2wContext.propertyKey,
      PotFiringKey(Right(d2wContext.pageConfiguration))
    )

  }

  def convertD2WContextToFullFledged(d2wContext: FiringD2WContext): D2WContextFullFledged = {
    //log.debug("D2WContextUtils.convertD2WContextToFullFledged : " + d2wContext.pageConfiguration)
    /*if(d2wContext.pageConfiguration.isDefined) {
      log.debug("D2WContextUtils.convertD2WContextToFullFledged : d2wContext.pageConfiguration.get " + d2wContext.pageConfiguration.get)
      log.debug("D2WContextUtils.convertD2WContextToFullFledged : d2wContext.pageConfiguration.get.right " + d2wContext.pageConfiguration.get.right)
      log.debug("D2WContextUtils.convertD2WContextToFullFledged : d2wContext.pageConfiguration.get.right.get " + d2wContext.pageConfiguration.get.right.get)

    }*/

    D2WContextFullFledged(
      d2wContext.entityName,
      d2wContext.task,
      d2wContext.propertyKey,
      d2wContext.pageConfiguration.value.right.get
    )
  }

}


// Kind of cache of entity task d2w rules
// Allows to change the menu without haveing to fetch the display property keys
//case class MetaDatas(entityMetaDatas: List[EntityMetaData])

sealed trait RulesContainer {
def ruleResults: List[RuleResult]
}

case class TaskFault(entityName: String, taskName: String, override val ruleResults: List[RuleResult] = List()) extends RulesContainer

// Property
case class PropertyMetaInfo(typeV: String = "stringV", name: String, entityName : String, task: String,
                        override val ruleResults: List[RuleResult] = List()) extends RulesContainer
//case class PropertyMetaInfo(d2WContext: D2WContext, value: StringValue, ruleKeyValues: Map[String,RuleResult] )

// A D2W Context of type page (without property)
case class EntityMetaData(d2wContext: D2WContextFullFledged, displayName: String, displayPropertyKeys: List[PropertyMetaInfo])


// Task
//case class Task(task: String, displayPropertyKeys: List[PropertyMetaInfo])
//case class Task(name: String, displayPropertyKeys: List[PropertyMetaInfo]) extends RulesContainer


/// Important NOTE: Seems that Map is not supported in case classes managed by boopickle

object EOModelUtils {
  def destinationEntity(eomodel: EOModel, entity: EOEntity, relationshipName: String) = {
    val relationshipOpt = relationshipNamed(eomodel, entity.name, relationshipName)
    relationshipOpt match {
      case Some(relationship) =>
        val destinationEntityName = relationship.destinationEntityName
        entityNamed(eomodel, destinationEntityName)
      case None =>
        None
    }
  }

  def relationshipNamed(eomodel: EOModel, entityName: String, relationshipName: String) = {
    val sourceEntityOpt = entityNamed(eomodel, entityName)
    sourceEntityOpt match {
      case Some(sourceEntity) =>
        sourceEntity.relationships.find(r => r.name.equals(relationshipName))
      case None => None
    }
  }

  def entityNamed(eomodel: EOModel, entityName: String) = eomodel.entities.find(e => e.name.equals(entityName))
}





