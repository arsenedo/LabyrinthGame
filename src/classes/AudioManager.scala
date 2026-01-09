package classes

import javax.sound.sampled.{AudioFormat, AudioSystem, Clip, Control, DataLine, Line, LineListener}

class AudioManager {
  var song: Clip = _


  def playAudio(file : String): Unit = {
    stopAudio()
    try {
      val url = getClass.getResource(file)
      if (url == null) {
        println("Impossible d'ouvrir le fichier son !")
        return
      }

      val audioInputStream = AudioSystem.getAudioInputStream(url)
      song = AudioSystem.getLine(new DataLine.Info(classOf[Clip], audioInputStream.getFormat)).asInstanceOf[Clip]
      song.open(audioInputStream)
      song.loop(-1)

      song.start()

    }
    catch {
      case e: Exception =>
        println("Erreur lors de la lecture du son")
    }
  }

  def stopAudio(): Unit = {
    if (song != null && song.isRunning){
      song.stop()
      song.close()
    }
  }
}
