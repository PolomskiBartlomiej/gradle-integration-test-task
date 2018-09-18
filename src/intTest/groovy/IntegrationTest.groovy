import spock.lang.Specification

class IntegrationTest extends Specification {

    def "Check Is toTest say true"() {
        given:
        def toTest = new ToTest()

        when:
        def bool = toTest.isTrue()

        then:
        bool

    }
}
