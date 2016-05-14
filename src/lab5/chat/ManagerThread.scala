package lab5.chat

import lab5.protos.ChatOperationProtos.ChatAction
import lab5.protos.ChatOperationProtos.ChatAction.ActionType
import org.jgroups.protocols.UDP
import org.jgroups.{JChannel, Message, ReceiverAdapter}

class ManagerThread(val channelsManager: ChannelsManager) extends Runnable {
  var running = true
  val channel = new JChannel(false)
  val stack = new StackProvider(new UDP()).stack
  channel.setProtocolStack(stack)
  channel.setReceiver(new ReceiverAdapter {
    override def receive(msg: Message) = {
      val chatAction = ChatAction.parseFrom(msg.getBuffer)
      val channel = chatAction.getChannel
      val nickname = chatAction.getNickname
      channelsManager.getChannelThread(channel) match {
        case None => println("no thread")
        case Some(x) =>
          val channelThread = x
          if (chatAction.getAction == ActionType.JOIN) channelThread.registerUser(nickname)
          else if (chatAction.getAction == ActionType.LEAVE) channelThread.unregisterUser(nickname)
      }
    }
  })
  stack.init()
  channel.connect("ChatManagement321123")

  def run() = {
    while(running) {}
    channel.close()
  }

  def sendJoinMessage(channel: String, nickname: String) = {
    val value = ChatAction
      .newBuilder()
      .setAction(ActionType.JOIN)
      .setChannel(channel)
      .setNickname(nickname)
      .build()
      .toByteArray
    val message = new Message(null, null, value)
    this.channel.send(message)
  }

  def sendLeaveMessage(channel: String, nickname: String) = {
    val value = ChatAction
      .newBuilder()
      .setAction(ActionType.LEAVE)
      .setChannel(channel)
      .setNickname(nickname)
      .build()
      .toByteArray
    val message = new Message(null, null, value)
    this.channel.send(message)
  }

  def stop() = running = false
}
