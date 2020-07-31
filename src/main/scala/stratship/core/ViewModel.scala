package stratship.core

import indigo.{GameTime, Outcome}

// Why final and case??
final case class ViewModel() {

}

object ViewModel {
  def initial: ViewModel =
    ViewModel()
}