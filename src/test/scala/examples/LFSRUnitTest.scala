package examples

import Chisel.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import example.LFSR

class LFSRUnitTester(c: LFSR) extends PeekPokeTester(c) {
  //init D0123456 to 1
  var D0123456 = 1
  for (t <- 0 until 63) {
    step(1)
    val bit = ((D0123456 >> 5) ^ (D0123456 >> 4)) & 1;
    D0123456 = (D0123456 << 1) | bit;
    if(D0123456>63){
      D0123456 = D0123456 - 64
    }
    //println(bit, D0123456)
    expect(c.io.out, D0123456)
  }
}

class LFSRTester extends ChiselFlatSpec {
  val backendNames = Array[String]("firrtl", "verilator")
  for ( backendName <- backendNames ) {
    "LFSR" should s"generate random numbers (with ${backendName})" in {
      Driver(() => new LFSR, backendName) {
        c => new LFSRUnitTester(c)
      } should be (true)
    }
  }
}
