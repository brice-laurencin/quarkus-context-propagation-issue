package resource

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.DiscriminatorMapping
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
class Greetings {

    @GET
    fun get() = Data()
}

data class Data(
    @field:Schema(
                    anyOf = [SomeThing::class, OtherThing::class],
                    type = SchemaType.ARRAY)
    val things: List<Thing> = listOf(SomeThing(), OtherThing()))

@Schema(
    description = "Thing",
    discriminatorProperty = "@type",
    discriminatorMapping = [
        DiscriminatorMapping(
            value = "SomeThing",
            schema = SomeThing::class
        ),
        DiscriminatorMapping(
            value = "OtherThing",
            schema = OtherThing::class
        ),
    ]
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
    value = [
        JsonSubTypes.Type(value = SomeThing::class, name = "SomeThing"),
        JsonSubTypes.Type(value = OtherThing::class, name = "OtherThing"),
    ]
)
open class Thing(open val thing: String)

@Schema(description = "Some thing")
data class SomeThing(
    val some: String = "some",
    @field:Schema(hidden = true)
    override val thing: String = "thing"
) : Thing(thing)

@Schema(description = "Other thing")
data class OtherThing(
    val other: String = "other",
    @field:Schema(hidden = true)
    override val thing: String = "thing"
) : Thing(thing)
