fun Any.objectTag(): String = "${this.javaClass.simpleName}@${this.hashCode()}"
fun Any.tag(): String = this.javaClass.simpleName