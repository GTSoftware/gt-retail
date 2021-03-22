import * as StringUtils from "../StringUtils"

describe("StringUtils", () => {
  describe("When calling isString", () => {
    it("should return true on string value", () => {
      let result = StringUtils.isString("hello")

      expect(result).toBe(true)
    })

    it("should return false on number value", () => {
      let result = StringUtils.isString(3)

      expect(result).toBe(false)
    })

    it("should return false on null value", () => {
      let result = StringUtils.isString(null)

      expect(result).toBe(false)
    })
  })
  describe("When calling toUpperCaseTrim", () => {
    it("should return uppercase string without trailing spaces", () => {
      let result = StringUtils.toUpperCaseTrim("   hello  ")

      expect(result).toBe("HELLO")
    })
  })

  describe("When calling toLowerCaseTrim", () => {
    it("should return lowercase string without trailing spaces", () => {
      let result = StringUtils.toLowerCaseTrim("   HELLO  ")

      expect(result).toBe("hello")
    })
  })

  describe("When calling isEmpty", () => {
    it("should return true on empty string", () => {
      let result = StringUtils.isEmpty("")

      expect(result).toBe(true)
    })
    it("should return false on non empty string", () => {
      let result = StringUtils.isEmpty("something")

      expect(result).toBe(false)
    })
  })
})
