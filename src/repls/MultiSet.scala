package repls

import repls.MultiSet.empty

case class MultiSet[T] (multiplicity: Map[T, Int]) { //multiplicity is the actual Multiset/map of our object

    def *(that: MultiSet[T]): MultiSet[T] = {
        val commonKeys = this.multiplicity.keySet intersect that.multiplicity.keySet //returns the SET of common keys (the set you would get from multiplying the multisets' sets (aka w/o dupes))

        val mutableElementsMap = collection.mutable.Map.empty[T, Int]

        for (key <- commonKeys) {
            val minCount = math.min(this.multiplicity(key), that.multiplicity(key))
             mutableElementsMap(key) = minCount
        }
        MultiSet(mutableElementsMap.toMap)
    }
    def +(that: MultiSet[T]): MultiSet[T] = {
        val allKeys = this.multiplicity.keySet ++ that.multiplicity.keySet //returns the SET of keys that appear in at least one multiset
        val mutableElementsMap = collection.mutable.Map.empty[T, Int]

        for (key <- allKeys) {
            val newCount = this.multiplicity.getOrElse(key, 0) + that.multiplicity.getOrElse(key, 0)
            mutableElementsMap(key) = newCount
        }
        MultiSet(mutableElementsMap.toMap)
    }

    def -(that: MultiSet[T]): MultiSet[T] = {
        val mutableElementsMap = collection.mutable.Map.empty[T, Int]

        for (key <- this.multiplicity.keySet) {
            val thisCount = this.multiplicity(key)
            val thatCount = that.multiplicity.getOrElse(key, 0)
            val newCount = math.max(thisCount - thatCount, 0)
            if (newCount > 0) mutableElementsMap(key) = newCount
        }
        MultiSet(mutableElementsMap.toMap)
    }

    def toSeq: Seq[T] = {
        val result = collection.mutable.ListBuffer.empty[T]

        for ((elem, count) <- multiplicity) {
            for (_ <- 1 to count) {
                result += elem
            }
        }

        result.toSeq
    }

    val MaxCountForDuplicatePrint = 5

    // A toString has already been provided
    override def toString: String = {
        def elemToString(elem : T) : String = {
            val count = multiplicity(elem)
            if(count >= MaxCountForDuplicatePrint)
                elem.toString + " -> " + count.toString
            else Seq.fill(count)(elem).mkString(",")
        }
        val keyStringSet = multiplicity.keySet.map(elemToString)
        "{" + keyStringSet.toSeq.sorted.mkString(",") + "}"
    }


}

object MultiSet {
    def empty[T] : MultiSet[T] = MultiSet(Map[T,Int]())
    //A "type" multiset is a map between type T and int, which represents sequences of {a,a,a,b,c,c} as: Map('a'->3, 'b'->1, 'c'->2)
    def apply[T](elements: Seq[T]): MultiSet[T] = {
        val mutableElementsMap = collection.mutable.Map.empty[T, Int]

        for (elem <- elements) {
            if (mutableElementsMap.contains(elem)) {
                mutableElementsMap(elem) = mutableElementsMap(elem) + 1
            }
            else {
                mutableElementsMap(elem) = 1
            }
        }

        MultiSet(mutableElementsMap.toMap)
    }
}
