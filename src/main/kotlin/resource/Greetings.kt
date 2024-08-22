package resource

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses

@Path("/")
class Greetings {

    @GET
    fun get() = listOf(Data())
}


@Schema(description = "The data")
data class Data(
    @field:Schema(
        type = SchemaType.ARRAY,
        anyOf = [SomeThing::class, OtherThing::class]
    )
    val things: List<Thing> = listOf(SomeThing(), OtherThing())
)

@Schema(description = "Some thing")
data class SomeThing(val some: String = "some", override val thing: String = "thing") : Thing
@Schema(description = "Other thing")
data class OtherThing(val other: String = "other", override val thing: String = "thing") : Thing

@Schema(description = "Thing")
interface Thing {
    val thing: String
}
