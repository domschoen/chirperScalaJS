package d2spa.client.components

import japgolly.univeq.UnivEq
import d2spa.client.components.Bootstrap.CommonStyle

import d2spa.client.CssSettings._
import scalacss.internal.mutable
import d2spa.client.components.Bootstrap.CommonStyle._

class BootstrapStyles(implicit r: mutable.Register) extends StyleSheet.Inline()(r) {

  import dsl._

  implicit val styleUnivEq: UnivEq[CommonStyle.Value] = new UnivEq[CommonStyle.Value] {}

  val csDomain = Domain.ofValues(default, primary, success, info, warning, danger)

  val contextDomain = Domain.ofValues(success, info, warning, danger)

  def commonStyle[A: UnivEq](domain: Domain[A], base: String) = styleF(domain)(opt =>
    styleS(addClassNames(base, s"$base-$opt"))
  )

  def styleWrap(classNames: String*) = style(addClassNames(classNames: _*))

  val buttonOpt = commonStyle(csDomain, "btn")

  val button = buttonOpt(default)

  val panelOpt = commonStyle(csDomain, "panel")

  val panel = panelOpt(default)

  val labelOpt = commonStyle(csDomain, "label")

  val label = labelOpt(default)

  val alert = commonStyle(contextDomain, "alert")

  val panelHeading = styleWrap("panel-heading")

  val panelBody = styleWrap("panel-body")

  // wrap styles in a namespace, assign to val to prevent lazy initialization
  object modal {
    val modal = styleWrap("modal")
    val fade = styleWrap("fade")
    val dialog = styleWrap("modal-dialog")
    val content = styleWrap("modal-content")
    val header = styleWrap("modal-header")
    val body = styleWrap("modal-body")
    val footer = styleWrap("modal-footer")
  }


  val _modal = modal

  object listGroup {
    val listGroup = styleWrap("list-group")
    val item = styleWrap("list-group-item")
    val itemOpt = commonStyle(contextDomain, "list-group-item")
    val active = styleWrap("active")
    val inputGroup = styleWrap("input-group")
  }

  val _listGroup = listGroup
  val pullRight = styleWrap("pull-right")
  val buttonXS = styleWrap("btn-xs")
  val close = styleWrap("close")

  val labelAsBadge = style(addClassName("label-as-badge"), borderRadius(1.em))

  val navbar = styleWrap("nav", "navbar-nav")

  val formGroup = styleWrap("form-group")
  val formControl = styleWrap("form-control")

  val ariahidden = styleWrap("nav", "navbar-nav")

}
