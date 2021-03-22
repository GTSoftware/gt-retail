import * as DateUtils from "../DateUtils"

const TEST_DATE = new Date(2020, 0, 15, 13, 21)

describe("DateUtils", () => {
  describe("When calling serializeDate", () => {
    it("should return date as string in ISO format", () => {
      let result = DateUtils.serializeDate(TEST_DATE)

      expect(result).toBe("2020-01-15T13:21:00")
    })
  })
})
