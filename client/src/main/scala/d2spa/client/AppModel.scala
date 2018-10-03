package d2spa.client

import diode._
import diode.data._
import diode.util._
import d2spa.shared.{EntityMetaData, _}
import boopickle.DefaultBasic._
import d2spa.client.logger.log
import d2spa.shared.WebSocketMessages.RuleToFire
import jdk.nashorn.internal.ir.PropertyKey
/**
  * Created by dschoen on 01.05.17.
  */





sealed trait RulesContainer {
  def ruleResults: List[RuleResult]
}

case class AppConfiguration(serverAppConf: DebugConf = DebugConf(true), isDebugMode: Boolean = false, fetchMenus: Boolean = true, socketReady: Boolean = false)

case class PageConfigurationRuleResults(override val ruleResults: List[RuleResult] = List(), metaDataFetched: Boolean = false, properties: Map[String,PropertyRuleResults] = Map()) extends RulesContainer
case class PropertyRuleResults(override val ruleResults: List[RuleResult] = List(), typeV: String = "stringV") extends RulesContainer
object PageConfiguration {
  val NoPageConfiguration = "NoPageConfiguration"
}

case class D2WContextEO(pk: Option[Int] = None, memID : Option[Int] = None)


// cache could be filled with an FS which could fill it partially
// how to know if it is filled for the FS we want ?
// store qualifier -> eos
// if if contains an entry for the qualifier or if partial a full exists

// data structure
// Map: entity --> EntityCache( full : Some(Map[EOPk,EO]]), partials : Map[EOQualifier, Map[EOPk,EO]])
// or
// Map: entity --> Map[EOQualifier, Map[EOPk,EO]])
// if only 1 qualifier and is EmptyQualifier
// EmptyQualifier has to be used also for search in that way we can merge fetch and fetch all

// server knows also qualifier --> eos
// if new eo or eo updated -> if validate for qualifier -> send update of qualifier --> eos

// mechanism for qualifier inclusion i.e if FetchAll not need to store something else
// not storing a partial if an all (at client and server) Note: if a partial is executed on the client and an full exists, it will not go to the server

// the server could decide to:
// return an all for a partial if not so many rows
// return a batch of a partial or full if too many rows

// concept of qualifier / Batch number --> eos

case class EOCache(eomodel: Pot[EOModel],
                   eos: Map[String, Map[EOPk,EO]],
                   insertedEOs: Map[String, Map[EOPk,EO]])


case class DebugConf(showD2WDebugButton: Boolean)
case class SendingAction(action: Action) extends Action

// define actions
case object ShowBusyIndicator extends Action
case object HideBusyIndicator extends Action
case class SearchWithBusyIndicator(entityName: String) extends Action

case object SocketReady extends Action
case object InitAppSpecificClient extends Action
case class InitMenuAndEO(eo: EO, missingKeys: Set[String]) extends Action
case class SetMenus(menus: Menus) extends Action
case class SetMenusAndEO(menus: Menus, eo: EO, missingKeys: Set[String]) extends Action
case class RefreshEO(eo:EO, rulesContainer: RulesContainer, actions: List[D2WAction]) extends Action
case class UpdateRefreshEOInCache(eos: Seq[EO], d2wContext: D2WContext, actions: List[D2WAction]) extends Action
case class UpdateEOInCache(eo:EO) extends Action
case object FetchEOModel extends Action
case object FetchEOModelAndMenus extends Action


case class SetEOModelThenFetchMenu(eomodel: EOModel) extends Action
case class SetEOModel(eomodel: EOModel) extends Action
case object FetchMenu extends Action

case class FetchedObjectsForEntity(eos: Seq[EO]) extends Action
case class RefreshedEOs(eos: Seq[EO])

case class InitMetaData(d2wContext: D2WContext) extends Action
case class InitMetaDataForList(entityName: String) extends Action
//case class SetPageForTaskAndEntity(d2wContext: D2WContext) extends Action
case class CreateEO(entityName:String) extends Action

case class SetMetaData(d2wContext: D2WContext, metaData: EntityMetaData) extends Action
//case class SetMetaDataForMenu(d2wContext: D2WContext, metaData: EntityMetaData) extends Action

case class NewAndRegisteredEO(d2wContext: D2WContext) extends Action
case class NewEOCreated(eo: EO, d2wContext: D2WContext, actions: List[D2WAction]) extends Action
case class NewEOCreatedForEdit(eo: EO) extends Action

case class InstallEditPage(fromTask: String, eo:EO) extends Action
case class InstallInspectPage(fromTask: String, eo:EO) extends Action
case object SetPreviousPage extends Action
case class RegisterPreviousPage(d2wContext: D2WContext) extends Action
case class RegisterPreviousPageAndSetPage(d2wContext: D2WContext) extends Action

case class SetPage(d2WContext: D2WContext) extends Action
case class UpdateCurrentContextWithEO(eo: EO) extends Action
case object InitMenuSelection extends Action

case object InitAppModel extends Action
case object ShowResults extends Action

case class SelectMenu(entityName: String) extends Action
case class Save(entityName: String, eo: EO) extends Action
case class SavingEO(eo: EO) extends Action
case class SaveNewEO(entityName: String, eo: EO) extends Action

case class UpdateQueryProperty(entityName: String, queryValue: QueryValue) extends Action
case class ClearQueryProperty(entityName: String, propertyName: String) extends Action
case class UpdateEOValueForProperty(eo: EO, d2wContext: D2WContext, value: EOValue) extends Action

case class SearchAction(entityName: String) extends Action
//case class SearchResult(entity: String, eos: Seq[EO]) extends Action
case class SearchResult(entityName: String, eos: Seq[EO]) extends Action
// similar to:
//case class UpdateAllTodos(todos: Seq[TodoItem]) extends Action

case class SavedEO(fromTask: String, eo: EO) extends Action
case class DeleteEO(fromTask: String, eo: EO) extends Action
case class DeleteEOFromList(eo: EO) extends Action
case class DeletingEO(eo: EO) extends Action
case class EditEO(fromTask: String, eo: EO) extends Action
case class InspectEO(fromTask: String, eo: EO, isOneRecord: Boolean = false) extends Action

case class SaveError(eo: EO) extends Action
object ListEOs extends Action
case class DeletedEO(eo:EO) extends Action
case class UpdateEOsForEOOnError(eo:EO) extends Action

trait D2WAction extends diode.Action
case class FireRule(rhs: D2WContext, key: String) extends D2WAction

// Typical case:
// key : componentName
// keysSubstrate: displayPropertyKeys either defined by a rule result or a rule to fire
// In that case the FireRules will trigger RuleToFire action to the server for as many entries in dipslayPropertyKeys
case class FireRules(propertyKeys: List[String], rhs: D2WContext, key: String) extends D2WAction
case class Hydration(drySubstrate: DrySubstrate,  wateringScope: WateringScope) extends D2WAction
case class CreateMemID(entityName: String) extends D2WAction
case class FetchMetaData(d2wContext: D2WContext) extends Action

case class FireActions(d2wContext: D2WContext, actions: List[D2WAction]) extends Action


object Hydration {

  // case class DrySubstrate(eosAtKeyPath: Option[EOsAtKeyPath] = None, eo: Option[EOFault] = None, fetchSpecification: Option[EOFetchSpecification] = None)
  def entityName(drySubstrate: DrySubstrate) = drySubstrate match {
    case DrySubstrate(Some(eosAtKeyPath), _ , _) => eosAtKeyPath.eo.entityName
    case DrySubstrate(_, Some(eoFault) , _) => eoFault.entityName
    case DrySubstrate(_, _ , Some(fs)) => EOFetchSpecification.entityName(fs)
  }

  def isEOHydrated(cache: EOCache, entityName: String, pk : EOPk, propertyKeys: List[String]) : Boolean = {
    val eoOpt = EOCacheUtils.outOfCacheEOUsingPk(cache, entityName, pk)
    eoOpt match {
      case Some(eo) =>
        val valuesKeys = eo.values
        val anyMissingPropertyKey = propertyKeys.find(p => !valuesKeys.contains(p))
        anyMissingPropertyKey.isEmpty
      case None => false
    }

  }

  def isHydratedForPropertyKeys(eomodel: EOModel, cache: EOCache, drySubstrate: DrySubstrate, propertyKeys: List[String]): Boolean = {
    drySubstrate match {
      case DrySubstrate(_, Some(eoFault), _) =>
        isEOHydrated(cache,eoFault.entityName, eoFault.pk, propertyKeys)
      case DrySubstrate(Some(eoakp), _, _) =>
        //log.debug("Hydration DrySubstrate " + eoakp.eo.entity.name + " for key " + eoakp.keyPath)
        val eovalueOpt = EOValue.valueForKey(eoakp.eo, eoakp.keyPath)
        //log.debug("Hydration DrySubstrate valueForKey " + eovalueOpt)

        eovalueOpt match {
          case Some(eovalue) =>
            eovalue match {
              case ObjectsValue(pks) =>
                //log.debug("NVListComponent render pks " + pks)
                val destinationEntityName = eoakp.destinationEntityName
                val nonHydrated = pks.find(pk => !isEOHydrated(cache, destinationEntityName, pk, propertyKeys))
                log.debug("AppModel | Hydration | isHydratedForPropertyKeys " + destinationEntityName + " at path: " + eoakp.keyPath + " any non hydrated " + nonHydrated)
                nonHydrated.isEmpty

              case _ => false
            }
          case _ => false
        }
      case DrySubstrate(_, _, Some(fs)) =>
        val eos = EOCacheUtils.objectsFromAllCachesWithFetchSpecification(cache,fs)
        val nonHydrated = eos.find(eo => !isEOHydrated(cache, eo.entityName, eo.pk, propertyKeys))
        !nonHydrated.isDefined
    }
  }

}

object QueryOperator {
  val Match = "Match"
  val Min = "Min"
  val Max = "Max"
}


// A container for value should be used. It would give a way to have not only String
case class QueryValue(key: String,value: EOValue, operator: String)





object QueryValue {
  val operatorByQueryOperator = Map(
    QueryOperator.Max -> NSSelector.QualifierOperatorLessThanOrEqualTo,
    QueryOperator.Min -> NSSelector.QualifierOperatorGreaterThanOrEqualTo,
    QueryOperator.Match -> NSSelector.QualifierOperatorEqual
  )

  def qualifierFromQueryValues(queryValues: List[QueryValue]) : Option[EOQualifier] = {
    val qualifiers = queryValues.map(qv => {
      log.debug("QueryValue " + qv)
      log.debug("QueryValue operatorByQueryOperator(qv.operator): " + operatorByQueryOperator(qv.operator))
      EOKeyValueQualifier(qv.key,operatorByQueryOperator(qv.operator),qv.value)
    })
    if (qualifiers.isEmpty) None else Some(EOAndQualifier(qualifiers))
  }

  def size(queryValue: QueryValue) = EOValue.size(queryValue.value)
}


//implicit val fireActionPickler = CompositePickler[FireAction].

// The D2WContext will contains also the queryValues
// it should contain everything needed to redisplay a page
case class D2WContext(entityName: Option[String],
                      task: Option[String],
                      previousTask: Option[D2WContext] = None,
                      //pageCounter: Int = 0,
                      eo: Option[EO] = None,
                      queryValues: Map[String, QueryValue] = Map(),
                      dataRep: Option[DataRep] = None,
                      propertyKey:  Option[String] = None,

                      // Either
                      // - defined by a rule not yet fired (RuleFault). It is a action which is before in the list
                      // - a value
                      // - None
                      pageConfiguration: PotFiredKey = PotFiredKey(Right(None)),
                      pk : Option[Int] = None)

// Right = Some
// here right is an result which could be Some or None
// left is not yet a result because it is a rule to be fired
case class PotFiredKey (value: Either[FireRule, Option[String]])
case class PotFiredRuleResult (value: Either[FireRule, RuleResult])

case class DataRep (fetchSpecification: Option[EOFetchSpecification] = None, eosAtKeyPath: Option[EOsAtKeyPath] = None)

object NSSelector {
  val QualifierOperatorEqual = "QualifierOperatorEqual"
  val QualifierOperatorNotEqual = "QualifierOperatorNotEqual"
  val QualifierOperatorLessThan = "QualifierOperatorLessThan"
  val QualifierOperatorGreaterThan = "QualifierOperatorGreaterThan"
  val QualifierOperatorGreaterThanOrEqualTo = "QualifierOperatorGreaterThanOrEqualTo"
  val QualifierOperatorLessThanOrEqualTo = "QualifierOperatorLessThanOrEqualTo"
  val QualifierOperatorContains = "QualifierOperatorContains"
  val QualifierOperatorLike = "QualifierOperatorLike"
  val QualifierOperatorCaseInsensitiveLike = "QualifierOperatorCaseInsensitiveLike"
}

object EOQualifierType {
  val EOAndQualifier = "EOAndQualifier"
  val EOOrQualifier = "EOOrQualifier"
  val EOKeyValueQualifier = "EOKeyValueQualifier"
  val EONotQualifier = "EONotQualifier"
}


case class KeysSubstrate(ruleResult: PotFiredRuleResult)


case class DrySubstrate(
                          eosAtKeyPath: Option[EOsAtKeyPath] = None,
                          eo: Option[EOFault] = None,
                          fetchSpecification: Option[EOFetchSpecification] = None
                       )
case class WateringScope(ruleResult: PotFiredRuleResult)
//case class EORefsDefinition()
case class EOsAtKeyPath(eo: EO, keyPath: String, destinationEntityName: String)
//case class RuleRawResponse(ruleFault: RuleFault, response: WSResponse)




object RuleUtils {

  def metaDataFetched(ruleResults: Map[String,Map[String,Map[String,PageConfigurationRuleResults]]], d2wContext: D2WContext): Boolean  = {
    val ruleContainerOpt = RuleUtils.pageConfigurationRuleResultsForContext(ruleResults,d2wContext)
    ruleContainerOpt match {
      case Some(rulesContainer) => rulesContainer.metaDataFetched
      case None => false
    }
  }

  def potentialFireRule(ruleResults: Map[String,Map[String,Map[String,PageConfigurationRuleResults]]], d2wContext: D2WContext, key: String) : Option[FireRule] = {
    val ruleResultOpt = RuleUtils.ruleStringValueForContextAndKey(ruleResults, d2wContext, key)
    ruleResultOpt match {
      case Some(_) => None
      case None =>
        Some(FireRule(d2wContext, key))
    }
  }




  def potentialFireRules(ruleResults: Map[String,Map[String,Map[String,PageConfigurationRuleResults]]],
                         d2wContext: D2WContext,
                         displayPropertyKeysRuleResultOpt: Option[RuleResult],
                         key: String) : Option[FireRules] = {

    displayPropertyKeysRuleResultOpt match {
      case Some(displayPropertyKeysRuleResult) =>
        val displayPropertyKeys = RuleUtils.ruleListValueWithRuleResult(displayPropertyKeysRuleResultOpt)
        if (displayPropertyKeys.size == 0) {
          None
        } else {
          val propertyD2WContext = d2wContext.copy(propertyKey = Some(displayPropertyKeys.head))
          val ruleResultOpt = RuleUtils.ruleResultForContextAndKey(ruleResults, propertyD2WContext, key)
          ruleResultOpt match {
            case Some(_) => None
            case None =>
              Some(FireRules(displayPropertyKeys, d2wContext, key))
          }
        }
      case None => None
    }
  }

  /*
    RuleFault(
        D2WContext(
            Some(Project),Some(list),None,None,Map(),None,None,
            // Page Configuration
            Some(
                Left(
                    RuleFault(
                        D2WContext(
                            Some(Project),Some(list),None,None,Map(),
                            Some(
                                DataRep(None,
                                    Some(
                                        EOsAtKeyPath(
                                            EO(
                                              EOEntity(
                                                Customer,id,List(EORelationship(projects,Project))
                                              ),
                                              Map(
                                                acronym -> StringValue(Some(8)),
                                                name -> StringValue(Some(8)),
                                                id -> IntValue(Some(8)),
                                                address -> StringValue(Some(8)),
                                                projects -> ObjectsValue(Vector(1)),
                                                type -> StringValue(Some(Customer))
                                              ),8,None)
                                            ,projects
                                        )
                                    )
                                )
                            ),
                            None,
                            None
                        )
                        ,listConfigurationName
                    )
                )
            )
        )
        ,displayPropertyKeys
    )
   */

  /*def fireRuleFault(ruleResults: Map[String,Map[String,Map[String,PageConfigurationRuleResults]]], ruleFault: RuleFault): Option[RuleResult] = {
    val ruleKey = ruleFault.key
    val ruleRhs = D2WContextUtils.d2wContextByResolvingRuleFaults(ruleResults,ruleFault.rhs)
    RuleUtils.ruleResultForContextAndKey(ruleResults,ruleRhs,ruleKey)
  }*/




  def ruleResultForContextAndKey(ruleResults: List[RuleResult], rhs: D2WContext, key: String) = ruleResults.find(r => {D2WContextUtils.isD2WContextEquals(r.rhs,rhs) && r.key.equals(key)})
  //def ruleResultForContextAndKey(ruleResults: List[RuleResult], rhs: D2WContext, key: String) = ruleResults.find(r => {r.key.equals(key)})

  def ruleStringValueForContextAndKey(ruleResults: Map[String,Map[String,Map[String,PageConfigurationRuleResults]]], d2wContext: D2WContext, key:String): Option[String] = {
    val ruleResult = ruleResultForContextAndKey(ruleResults, d2wContext, key)
    ruleStringValueWithRuleResult(ruleResult)
  }

  def ruleBooleanValueForContextAndKey(ruleResults: Map[String,Map[String,Map[String,PageConfigurationRuleResults]]], d2wContext: D2WContext, key:String): Boolean = {
    val booleanAsStringOpt = RuleUtils.ruleStringValueForContextAndKey(ruleResults, d2wContext, key)
    booleanAsStringOpt match {
      case Some(boolVal) => boolVal.equals("true")
      case None => false
    }
  }

  def ruleListValueForContextAndKey(ruleResults: Map[String,Map[String,Map[String,PageConfigurationRuleResults]]], d2wContext: D2WContext, key:String): List[String] = {
    val ruleResult = ruleResultForContextAndKey(ruleResults, d2wContext, key)
    ruleListValueWithRuleResult(ruleResult)
  }

  def pageConfigurationWithRuleResult(propertyKeyOpt: Option[String],ruleResult: RuleResult) = {
    propertyKeyOpt match {
      case Some(propertyKey) => {
        PageConfigurationRuleResults(properties = Map(propertyKey -> PropertyRuleResults(ruleResults = List(ruleResult))))
      }
      case _ => {
        PageConfigurationRuleResults(ruleResults = List(ruleResult))
      }
    }
  }


  def registerRuleResult(ruleResults: Map[String,Map[String,Map[String,PageConfigurationRuleResults]]], ruleResult: RuleResult): Map[String,Map[String,Map[String,PageConfigurationRuleResults]]] = {
    val d2wContext = ruleResult.rhs
    val entityName = d2wContext.entityName.get
    val task = d2wContext.task.get
    val pageConfiguration: String = if (d2wContext.pageConfiguration.isDefined) d2wContext.pageConfiguration.get else PageConfiguration.NoPageConfiguration


    val taskSubTree = if (ruleResults.contains(entityName)) {
      val existingTaskSubTree = ruleResults(entityName)
      val pageConfigurationSubTree = if (existingTaskSubTree.contains(task)) {
        val existingPageConfigurationSubTree = existingTaskSubTree(task)
        val pageConfigurationRuleResults = if (existingPageConfigurationSubTree.contains(pageConfiguration)) {
          val existingPageConfigurationRuleResults = existingPageConfigurationSubTree(pageConfiguration)
          d2wContext.propertyKey match {
            case Some(propertyKey) => {
              val propertyRuleResults = if (existingPageConfigurationRuleResults.properties.contains(propertyKey)) {
                val existingPropertyRuleResults = existingPageConfigurationRuleResults.properties(propertyKey)
                existingPropertyRuleResults.copy(ruleResults = ruleResult :: existingPropertyRuleResults.ruleResults )
              } else {
                PropertyRuleResults(ruleResults = List(ruleResult))
              }
              val newProperties = existingPageConfigurationRuleResults.properties + (propertyKey -> propertyRuleResults)
              existingPageConfigurationRuleResults.copy(properties = newProperties)
            }
            case _ => {
              existingPageConfigurationRuleResults.copy(ruleResults = ruleResult :: existingPageConfigurationRuleResults.ruleResults)
            }
          }
        } else {
          pageConfigurationWithRuleResult(d2wContext.propertyKey,ruleResult)
        }
        val flaggedPageConfigurationRuleResults = if (ruleResult.key.equals(RuleKeys.displayNameForEntity)) pageConfigurationRuleResults.copy(metaDataFetched = true) else pageConfigurationRuleResults
        existingPageConfigurationSubTree + (pageConfiguration -> flaggedPageConfigurationRuleResults)
      } else {
        val pageConfigurationRuleResults = pageConfigurationWithRuleResult(d2wContext.propertyKey,ruleResult)
        val flaggedPageConfigurationRuleResults = if (ruleResult.key.equals(RuleKeys.displayNameForEntity)) pageConfigurationRuleResults.copy(metaDataFetched = true) else pageConfigurationRuleResults
        Map(pageConfiguration -> flaggedPageConfigurationRuleResults)
      }
      existingTaskSubTree + (task -> pageConfigurationSubTree)
    } else {
      val pageConfigurationRuleResults = pageConfigurationWithRuleResult(d2wContext.propertyKey,ruleResult)
      val flaggedPageConfigurationRuleResults = if (ruleResult.key.equals(RuleKeys.displayNameForEntity)) pageConfigurationRuleResults.copy(metaDataFetched = true) else pageConfigurationRuleResults

      Map(task -> Map(pageConfiguration -> flaggedPageConfigurationRuleResults))
    }
    ruleResults + (entityName -> taskSubTree)
  }

  def pageConfigurationRuleResultsForContext(ruleResults: Map[String,Map[String,Map[String,PageConfigurationRuleResults]]], d2wContext: D2WContext): Option[PageConfigurationRuleResults] = {
    val entityName = d2wContext.entityName.get
    val task = d2wContext.task.get
    val pageConfiguration: String = if (d2wContext.pageConfiguration.value.isRight) {
      d2wContext.pageConfiguration.value.right.get match {
        case Some(pc) => pc
        case None => PageConfiguration.NoPageConfiguration
      }
    } else PageConfiguration.NoPageConfiguration
    if (ruleResults.contains(entityName)) {
      val entitySubTree = ruleResults(entityName)
      if (entitySubTree.contains(task)) {
        val taskSubTree = entitySubTree(task)
        if (taskSubTree.contains(pageConfiguration)) {
          val pageConfigurationSubTree = taskSubTree(pageConfiguration)
          Some(pageConfigurationSubTree)
        } else None
      } else None
    } else None
  }

  def ruleContainerForContext(ruleResults: Map[String,Map[String,Map[String,PageConfigurationRuleResults]]], d2wContext: D2WContext): Option[RulesContainer] = {
    val pageConfigurationRuleResultsOpt = pageConfigurationRuleResultsForContext(ruleResults,d2wContext)
    pageConfigurationRuleResultsOpt match {
      case Some(pageConfigurationRuleResults) =>
        d2wContext.propertyKey match {
          case Some(propertyKey) => {
            if (pageConfigurationRuleResults.properties.contains(propertyKey)) {
              Some(pageConfigurationRuleResults.properties(propertyKey))
            } else {
              None
            }
          }
          case _ => {
            Some(pageConfigurationRuleResults)
          }
        }
      case _ => None
    }
  }

  def ruleResultForContextAndKey(ruleResults: Map[String,Map[String,Map[String,PageConfigurationRuleResults]]], d2wContext: D2WContext, key:String): Option[RuleResult] = {
    val ruleContainerOpt = ruleContainerForContext(ruleResults,d2wContext)
    ruleContainerOpt match {
      case Some(rulesContainer) => {
        ruleResultForContextAndKey(rulesContainer.ruleResults,d2wContext,key)
      }
      case _ => None
    }
  }


  def ruleStringValueWithRuleResult(ruleResultOpt: Option[RuleResult]) = {
    ruleResultOpt match {
      case Some(ruleResult) => ruleResult.value.stringV
      case _ => None
    }
  }
  def ruleListValueWithRuleResult(ruleResultOpt: Option[RuleResult]) = {
    ruleResultOpt match {
      case Some(ruleResult) => ruleResult.value.stringsV
      case _ => List()
    }
  }

  def existsRuleResultForContextAndKey(ruleResults: Map[String,Map[String,Map[String,PageConfigurationRuleResults]]], d2wContext: D2WContext, key:String) =
    ruleResultForContextAndKey(ruleResults, d2wContext, key).isDefined


}

object D2WContextUtils {


  def queryValueForKey(d2wContext: D2WContext, key: String) = {
    val queryValues = d2wContext.queryValues
    if (queryValues.contains(key)) Some(queryValues(key).value) else None
  }

  def queryValueAsStringForKey(d2wContext: D2WContext, key: String) = {
    val value = D2WContextUtils.queryValueForKey(d2wContext, key)
    val strValue = value match {
      case Some(StringValue(str)) => str
      case _ => ""
    }
  }

  def convertToPotFiringKey(potFiredKey: PotFiredKey): PotFiringKey = {
    potFiredKey.value match {
      case Right(b) => PotFiringKey(Right(b))
      case Left(fireRule) => {
        val newRhs = D2WContextUtils.convertFromD2WContextToFiringD2WContext(fireRule.rhs)
        PotFiringKey(Left(RuleToFire(newRhs,fireRule.key)))
      }
    }
  }




  def convertFromD2WContextToFiringD2WContext(d2wContext: D2WContext): FiringD2WContext = {
    FiringD2WContext(
        d2wContext.entityName,
        d2wContext.task,
        d2wContext.propertyKey,
        convertToPotFiringKey(d2wContext.pageConfiguration)
    )
  }

  def convertD2WContextToFullFledged(d2wContext: D2WContext): D2WContextFullFledged = {
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

  def convertFullFledgedToD2WContext(d2wContext: D2WContextFullFledged) = D2WContext(
    d2wContext.entityName,
    d2wContext.task,
    None,
    None,
    Map(),
    None,
    d2wContext.propertyKey,
    PotFiredKey(Right(d2wContext.pageConfiguration))
  )


  def isD2WContextEquals(a: D2WContextFullFledged, b: D2WContext): Boolean  = {
    if (!a.entityName.equals(b.entityName)) return false
    if (!a.task.equals(b.task)) return false
    if (!a.propertyKey.equals(b.propertyKey)) return false
    if (!(b.pageConfiguration.value.isRight && a.pageConfiguration.equals(b.pageConfiguration.value.right.get))) return false
    return true
  }

}


case class SetRuleResults(ruleResults: List[RuleResult], d2wContext: D2WContext, actions: List[D2WAction]) extends Action
case class SetJustRuleResults(ruleResults: List[RuleResult]) extends Action



case class FireRelationshipData(property: PropertyMetaInfo) extends Action

case class ShowPage(entity: EOEntity, task: String) extends Action
case class SetupQueryPageForEntity(entityName: String) extends Action

case object SwithDebugMode extends Action
case object FetchShowD2WDebugButton extends Action
case class SetDebugConfiguration(debugConf: DebugConf) extends Action

object EOCacheUtils {

  def removeEOFromCache(eo: EO, eos: Map[String, Map[EOPk, EO]]): Map[String, Map[EOPk, EO]] = {
    val entityName = eo.entityName
    val id = eo.pk
    val entityCache = eos(entityName)
    val updatedEntityCache = entityCache - id
    val updatedCache = eos + (entityName -> updatedEntityCache)
    updatedCache
  }

  //EOValueUtils.pk(eo)
  def newUpdatedCache(eomodel: EOModel, cache: Map[String, Map[EOPk, EO]], eos: Seq[EO]): Map[String, Map[EOPk, EO]] = {
    val anyHead = eos.headOption
    anyHead match {
      case Some(head) =>
        val entityName = head.entityName
        val entity = EOModelUtils.entityNamed(eomodel,entityName).get
        val pkAttributeNames = entity.pkAttributeNames
        val entityMap = if (cache.contains(entityName)) cache(entityName) else Map.empty[EOPk, EO]

        // we create a Map with eo and id
        // From eos to update, we create a map of key ->
        val refreshedEOs = eos.map(eo => {
          val pk = eo.pk
          Some((pk, eo))
        }).flatten.toMap

        // work with new and eo to be updated
        val refreshedPks = refreshedEOs.keySet
        val existingPks = entityMap.keySet

        val newPks = refreshedPks -- existingPks

        // Iterate on eos
        val newAndUpdateMap = refreshedPks.map(id => {
          //log.debug("Refresh " + entityName + "[" + id + "]")
          val refreshedEO = refreshedEOs(id)
          //log.debug("Refreshed " + refreshedEO)

          // 2 cases:
          // new
          if (newPks.contains(id)) {
            (id, refreshedEO)
          } else {
            // existing -> to be updated
            // Complete EOs !
            val existingEO = entityMap(id)
            (id, EOValue.completeEoWithEo(existingEO, refreshedEO))
          }
        }).toMap
        val newEntityMap = entityMap ++ newAndUpdateMap

        cache + (entity.name -> newEntityMap)
      case _ => Map.empty[String, Map[EOPk, EO]]
    }
  }



  // Entity Name is retreived from the eos
  def updatedModelForEntityNamed(eomodel: EOModel,cache: Map[String, Map[EOPk, EO]], eos: Seq[EO]): Map[String, Map[EOPk, EO]] = {
    if (eos.isEmpty) {
      cache
    } else {
      println("Zcache " + cache)
      println("Zcache " + eos)
      println("Zresult before " + cache)


      val result: Map[String, Map[EOPk, EO]] = newUpdatedCache(eomodel, cache, eos)
      println("Zresult " + result)

      //log.debug("storing result as :" + result)
      result
    }
  }



  def updatedOutOfDBCacheWithEOs(eomodel: EOModel, cache: EOCache, eos: Seq[EO]): EOCache = {
    //log.debug("Cache before update " + cache)
    val insertedEOs = cache.insertedEOs
    val outOfDBEOs = updatedModelForEntityNamed(eomodel, cache.eos, eos)
    val newCache = EOCache(Ready(eomodel), outOfDBEOs, insertedEOs)
    //log.debug("New cache " + newCache)
    newCache
  }

  // Returns None if nothing registered in the cache for that entityName
  def objectsForEntityNamed(eos: Map[String, Map[EOPk,EO]], entityName: String): Option[List[EO]] = if (eos.contains(entityName)) {
    val entityEOs = eos(entityName).values.toList
    Some(entityEOs)
  } else None

  def objectForEntityNamedAndPk(eos: Map[String, Map[EOPk,EO]], entityName: String, pk: EOPk): Option[EO] =
    if (eos.contains(entityName)) {
      val entityEOById = eos(entityName)

      if (entityEOById.contains(pk)) Some(entityEOById(pk)) else None
    } else None

  def objectsWithFetchSpecification(eos: Map[String, Map[EOPk,EO]],fs: EOFetchSpecification) : Option[List[EO]] = {
    val entityNameEOs = objectsForEntityNamed(eos,EOFetchSpecification.entityName(fs))
    //log.debug("EOCacheUtils.objectsWithFetchSpecification : " + entityNameEOs)
    entityNameEOs match {
      case Some(eos) =>
        Some(EOFetchSpecification.objectsWithFetchSpecification(eos,fs))
      case _ => None
    }
  }

  def outOfCacheEOsUsingPkFromEOs(cache: EOCache, entityName: String, eos: List[EO]): List[EO] = {
    eos.map(eo => outOfCacheEOUsingPkFromD2WContextEO(cache,entityName,eo)).flatten
  }

  def objectsFromAllCachesWithFetchSpecification(cache: EOCache,fetchSpecification: EOFetchSpecification): List[EO] = {
    val eosOpt = objectsWithFetchSpecification(cache.eos,fetchSpecification)
    val insertedEOsOpt = objectsWithFetchSpecification(cache.insertedEOs,fetchSpecification)
    println("insertedEOsOpt objectsFromAll.. " + insertedEOsOpt)

    eosOpt match {
      case Some(eos) =>
        insertedEOsOpt match {
          case Some(insertedEOs) =>
            // TODO sorting
            eos ++ insertedEOs
          case _ => eos
        }
      case _ => List.empty[EO]
    }

  }


  def outOfCacheEOUsingPkFromD2WContextEO(cache: EOCache, entityName: String, eo: EO): Option[EO] = {
    outOfCacheEOUsingPk(cache, entityName, eo.pk)
  }

  def outOfCacheEOUsingPks(cache: EOCache, entityName: String, pks: List[EOPk]): Seq[EO] = {
    pks.map(pk => outOfCacheEOUsingPk(cache, entityName, pk)).flatten
  }

  def outOfCacheEOUsingPk(cache: EOCache, entityName: String, pk: EOPk): Option[EO] = {
    val isInMemory = EOValue.isNew(pk)
    if (isInMemory) {
      val memCache = cache.insertedEOs
      log.debug("Out of cache " + pk)
      log.debug("Cache " + memCache)
      log.debug("e name " + entityName)
      EOCacheUtils.objectForEntityNamedAndPk(memCache, entityName, pk)
    } else {
      val dbCache = cache.eos
      //log.debug("DB Cache " + dbCache + " entityName " + entityName + " pk " + pk)
      EOCacheUtils.objectForEntityNamedAndPk(dbCache, entityName, pk)
    }
  }
}

