package lab5.chat

class ChatThread(val nickname: String) extends Runnable {
  def run(): Unit = {
    val resolver = new UserInputResolver(nickname)
    while (true) {
      for (ln <- io.Source.stdin.getLines()) {
          resolver.matchInput(ln)
      }
    }
  }
}
