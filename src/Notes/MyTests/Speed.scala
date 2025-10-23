package Notes.MyTests

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer



object Speed {
  case class Time(daysSinceEpoch : Int, hours : Int, minutes : Int, seconds : Double){
    def toSeconds: Double = (((daysSinceEpoch * 24 + hours) * 60) + minutes) * 60 + seconds
  }
  // case class means (among other things) that you do not have to type new to create one
  // so instead of new Time(43,6,3,0) you just type Time(43,6,3,0)
  // equality and pretty printing are also defined for you


  case class Observation(cameraSet : String, licensePlate : String, time : Time) {
  }

  // to convert your speed of type double to an Int use Math.round(speed).toInt
  case class SpeedOffender(licensePlate : String, speed : Int){
  }

  def speedOffenders(observations: Seq[Observation]) : ArrayBuffer[SpeedOffender] = {
    val startTimeOfCar : mutable.Map[String,Time] = new mutable.HashMap()
    val result :  ArrayBuffer[SpeedOffender] = new ArrayBuffer()

    for(observation <- observations) {
      if (observation.cameraSet == "A") {
        startTimeOfCar.update(observation.licensePlate, observation.time)
      }
      else if (observation.cameraSet == "B") {
        val startTime = startTimeOfCar(observation.licensePlate)
        val timeDifference = observation.time.toSeconds - startTime.toSeconds

        val speed = Math.round((1500 * 3.6) / timeDifference).toInt

        if ((observation.time.hours >= 6 && observation.time.hours < 19 && speed > 100) ||
          (observation.time.hours >= 19 || observation.time.hours < 6 && speed > 120)) {
          result += SpeedOffender(observation.licensePlate, speed)
        }
      }
    }
    result
  }
}

/*
import warmup.Speed
import warmup.Speed.SpeedOffender
import warmup.Speed.Observation
import warmup.Speed.Time

Observation("A","NX-66-PP",Time(18492, 12,6,0))
Observation("B","NX-66-PP",Time(18492, 12,6,50))

*/



// jaap.name
//jaap.rating