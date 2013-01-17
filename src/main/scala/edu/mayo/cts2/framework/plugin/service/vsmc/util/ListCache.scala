package edu.mayo.cts2.framework.plugin.service.vsmc.util

import java.util.{TimerTask, Timer}

class ListCache[T](expireMinutes: Long, retrieve: () => Seq[T]) {

  def this(retrieve: () => Seq[T]) = this(60, retrieve)

  val mutex: AnyRef = new Object()

  var cache: Seq[T] = retrieve()

  val clearCacheTask = new TimerTask() {

    override
    def run() {
      mutex.synchronized {
        cache = retrieve()
      }
    }
  }

  val millis: Long = fromMinutes(expireMinutes)

  new Timer().schedule(clearCacheTask, millis, millis)

  def get() = {
    mutex.synchronized {
      cache
    }
  }

  private def fromMinutes(minutes: Long): Long = {
    minutes * 60 * 1000
  }

}
