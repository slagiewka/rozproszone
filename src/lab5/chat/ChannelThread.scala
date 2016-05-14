package lab5.chat

import java.io.{DataInputStream, DataOutputStream, InputStream, OutputStream}
import java.net.InetAddress
import java.util

import lab5.protos.ChatOperationProtos.{ChatMessage, ChatState}
import org.jgroups.protocols.UDP
import org.jgroups.util.Util
import org.jgroups._

import scala.collection.JavaConversions._

class ChannelThread(val channelName: String) extends Runnable {
  var running = true
  val channel = new JChannel(false)
  val stack = new StackProvider(new UDP().setValue("mcast_group_addr", InetAddress.getByName("230.0.0." + channelName))).stack
  channel.setProtocolStack(stack)
  channel.setReceiver(new ReceiverAdapter {
    var state: ChatState = null
    var oldView = new View()
    override def viewAccepted(view: View) = {
      usersAddress = view.getMembers
      println("view accepted: " + view.toString)
    }

    override def getState(output: OutputStream) = {
      state.synchronized {
        Util.objectToStream(state, new DataOutputStream(output)).asInstanceOf
      }
    }

    override def setState(input: InputStream) = {
      val chatState: ChatState = Util.objectFromStream(new DataInputStream(input)).asInstanceOf[ChatState]
      state.synchronized {
        state = chatState
      }

//      println("received state (" + chatState.getStateList.size() + " messages in chat history):")
//      for(str <- chatState.getStateList) {
//        println(str)
//      }
    }

    override def receive(msg: Message) = {
      val message = ChatMessage.parseFrom(msg.getBuffer)
      println(message)
    }
  })
  stack.init()
  channel.connect(channelName)

  var usersAddress: util.List[Address] = new util.ArrayList[Address]()
  var users: Set[String] = Set()

  def run() = {
    while (running) {}
    channel.close()
  }

  def registerUser(name: String) = {
    users += name
  }

  def unregisterUser(name: String) = {
    users -= name
  }

  def sendMessage(message: String) = {
    val messageBytes = ChatMessage
      .newBuilder()
      .setMessage(message)
      .build()
      .toByteArray
    val jMessage = new Message(null, null, messageBytes)
    channel.send(jMessage)
  }

  def stop() = running = false
}
