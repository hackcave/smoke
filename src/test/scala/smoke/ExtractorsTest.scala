package smoke

import org.scalatest.FunSpec

class ExtractorsTest extends FunSpec {
  describe("Test Seg") {
    it("should segment cleanly") {
      val list = Seg.unapply("/1/2/3/4/5/6/7")
      expectResult(7)(list.get.size)
    }

    it("should segment removing empty pieces") {
      val list = Seg.unapply("/1/2/3/4/5/6//7")
      expectResult(7)(list.get.size)
    }

    it("should segment removing many empty pieces") {
      val list = Seg.unapply("/1/2/3/4/5/6///7")
      expectResult(7)(list.get.size)
    }

    it("should segment removing empty pieces and multiple leading '/'") {
      val Seg(list) = "//1/2/3/4/5/6///7"
      expectResult(7)(list.size)
    }
  }

  describe("Filename") {

    it("should extract filenames with multiple periods") {
      expectResult(Some("foo.bar.baz", "m3u8"))(Filename.unapply("/path/foo.bar.baz.m3u8"))
    }

    it("should extract filenames with path segments") {
      expectResult(Some("baz", "m3u8"))(Filename.unapply("/foo/bar/baz.m3u8"))
    }

    it("should extract a filename with no extension") {
      expectResult(Some("foo", ""))(Filename.unapply("foo"))
    }

    it("should extract a filename ending with a period but no extension") {
      expectResult(Some("foo", ""))(Filename.unapply("foo."))
    }

    it("should return None for an empty string") {
      expectResult(None)(Filename.unapply(""))
    }
  }

  describe("Test Filename.extension") {
    it("should return a file extension") {
      expectResult(Some("m3u8"))(Filename.extension.unapply("foo.m3u8"))
    }

    it("should extract extension with multiple periods") {
      expectResult(Some("m3u8"))(Filename.extension.unapply("foo.bar.baz.m3u8"))
    }

    it("should extract extension in a path") {
      expectResult(Some("m3u8"))(Filename.extension.unapply("/foo/path/foo.m3u8"))
    }

    it("should return none if the file does not have extension") {
      expectResult(None)(Filename.extension.unapply("/foo/path/foo"))
    }

    it("should return none if the filename ends with a period") {
      expectResult(None)(Filename.extension.unapply("/foo/path/foo."))
    }

    it("should not return a file extension") {
      expectResult(None)(Filename.extension.unapply("foo"))
    }
  }

  describe("Filename.base") {

    it("should extract basename with multiple periods") {
      expectResult(Some("foo.bar.baz"))(Filename.base.unapply("/path/foo.bar.baz.m3u8"))
    }

    it("should extract basename with path segments") {
      expectResult(Some("baz"))(Filename.base.unapply("/foo/bar/baz.m3u8"))
    }

    it("should extract a basename with no extension") {
      expectResult(Some("foo"))(Filename.base.unapply("foo"))
    }

    it("should extract a basename ending with a period but no extension") {
      expectResult(Some("foo"))(Filename.base.unapply("foo."))
    }

    it("should return None when it's an extension only") {
      expectResult(None)(Filename.base.unapply(".foo"))
    }

    it("should return None for an empty string") {
      expectResult(None)(Filename.base.unapply(""))
    }
  }

  describe("Test Method") {
    it("apply should return whether a request matches that method") {
      val req = new test.TestRequest("http://test.com")
      assert(GET(req))
      assert(!POST(req))
      assert(!PUT(req))
    }
  }
}
