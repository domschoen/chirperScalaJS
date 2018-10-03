package d2spa.client.components


import d2spa.client.D2WContext
import d2spa.client.logger.D2SpaLogger
import d2spa.shared.{EO, EOValue, PropertyMetaInfo}
import diode.react.ModelProxy
import diode.Action
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.ext.KeyCode
import scalacss.ScalaCssReact._
//import d2spa.client.css.GlobalStyle

import d2spa.client.SPAMain.{TaskAppPage}
import d2spa.client.MegaContent
import d2spa.client.UpdateQueryProperty
import d2spa.shared.{PropertyMetaInfo, EOValue}
import d2spa.client.{MegaContent, UpdateEOValueForProperty}

import d2spa.client.logger.log


object ERD2WDisplayString {

  //@inline private def bss = GlobalStyles.bootstrapStyles
  //bss.formControl,
  case class Props(router: RouterCtl[TaskAppPage], d2wContext: D2WContext, proxy: ModelProxy[MegaContent])


  class Backend($: BackendScope[Props, Unit]) {

    def render(p: Props) = {
      val d2wContext = p.d2wContext
      val entityName = d2wContext.entityName.get

      val eoOpt = d2wContext.eo
      //D2SpaLogger.logDebug( entityName, "Render ERD2WDisplayString eoOpt: " + eoOpt)
      eoOpt match {
        case Some(eo) =>
          //D2SpaLogger.logDebug( entityName, "Render ERD2WDisplayString eo: " + eo)

          val propertyName = d2wContext.propertyKey.get
          if (eo.keys.contains(propertyName)) {
            // We expect a value for that property. Either:
            // StringValue
            // EmptyValue

            val eoValue = EOValue.valueForKey(eo,propertyName).get
            val value = EOValue.juiceString(eoValue)
            <.div(
              <.span(^.id := "description", value)
            )
          } else {
            <.div(
              <.span(^.id := "description", "No data found")
            )

          }
        case None =>
          <.div(
            <.span(^.id := "description", "No eo found")
          )

      }

    }

  }

  private val component = ScalaComponent.builder[Props]("ERD2WDisplayString")
    .renderBackend[Backend]
    .build

  def apply(ctl: RouterCtl[TaskAppPage], d2wContext: D2WContext, proxy: ModelProxy[MegaContent]) = component(Props(ctl, d2wContext, proxy))

}
