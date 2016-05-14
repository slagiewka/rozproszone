package lab5.chat

object Chat extends App {
  assert(args.length == 1)
  val nickname = args(0)

  new Thread(new ChatThread(nickname)).start()
}