package lab5.chat

import org.jgroups.protocols._
import org.jgroups.protocols.pbcast._
import org.jgroups.stack.{Protocol, ProtocolStack}

class StackProvider(val udp: Protocol) {
  val stack = new ProtocolStack()
    .addProtocol(udp)
    .addProtocol(new PING())
    .addProtocol(new MERGE2())
    .addProtocol(new FD_SOCK())
    .addProtocol(new FD_ALL().setValue("timeout", 12000).setValue("interval", 3000))
    .addProtocol(new VERIFY_SUSPECT())
    .addProtocol(new BARRIER())
    .addProtocol(new NAKACK())
    .addProtocol(new UNICAST2())
    .addProtocol(new STABLE())
    .addProtocol(new GMS())
    .addProtocol(new UFC())
    .addProtocol(new MFC())
    .addProtocol(new FRAG2())
    .addProtocol(new STATE_TRANSFER())
    .addProtocol(new FLUSH())
}
