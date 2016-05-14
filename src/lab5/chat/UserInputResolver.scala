package lab5.chat

class UserInputResolver(val nickname: String) {
  val channelsManager = new ChannelsManager(nickname)

  def matchInput(input: String) = {
    input match {
      case in if in.startsWith("join ") => join(in.split("join ")(1))
      case in if in.startsWith("list channels") => listChannels()
      case in if in.startsWith("list users ") => listUsers(in.split("list users ")(1))
      case in if in.startsWith("send ") => sendMessageTo(in.split(" ")(1), in.split(" ")(2))
      case in if in.startsWith("leave ") => leaveChannel(in.split("leave ")(1))
      case in if in.startsWith("quit") => quit()

      case _ => help()
    }
  }

  def listChannels() = channelsManager.listChannels()

  def join(channelName: String) = channelsManager joinChannel channelName

  def listUsers(channel: String) = channelsManager.getChannelThread(channel).get.users.foreach((name: String) => println(name))

  def sendMessageTo(channelName: String, message: String) = channelsManager.getChannelThread(channelName).get.sendMessage(message)

  def leaveChannel(channelName: String) = channelsManager leaveChannel channelName

  def quit() = channelsManager quit()

  def help() = {
    println("Available commands:")
    println("join <channel (0-200)>")
    println("list channels")
    println("list users <channel>")
    println("send <channel> <message>")
    println("leave <channel>")
    println("quit")
    println("help")
  }
}
