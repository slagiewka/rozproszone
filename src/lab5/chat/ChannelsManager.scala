package lab5.chat

class ChannelsManager(val nickname: String) {
  var joinedChannels: Map[String, ChannelThread] = Map()
  val managerThread = new ManagerThread(this)
  new Thread(managerThread).start()

  def listChannels() = joinedChannels.keys.foreach((name: String) => println(name))

  def joinChannel(channelName: String): ChannelThread = {
    if (joinedChannels.contains(channelName)) {
      return joinedChannels(channelName)
    }

    val channelThread = new ChannelThread(channelName)
    val thread = new Thread(channelThread)
    thread.start()
    managerThread.sendJoinMessage(channelName, nickname)
    joinedChannels += (channelName -> channelThread)

    channelThread
  }

  def getChannelThread(channelName: String): Option[ChannelThread] = {
    joinedChannels.get(channelName)
  }

  def leaveChannel(channelName: String) = {
    if (joinedChannels.contains(channelName)) {
      managerThread.sendLeaveMessage(channelName, nickname)
      joinedChannels.get(channelName).get.stop()
      joinedChannels -= channelName
    }
  }

  def quit() = {
    joinedChannels.keys.foreach((name: String) => leaveChannel(name))
    managerThread.stop()
  }
}
