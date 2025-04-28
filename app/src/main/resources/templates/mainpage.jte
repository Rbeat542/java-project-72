@import hexlet.code.repository.UrlRepository
@import hexlet.code.util.NamedRoutes

@template.otherpages(
title = @`
    Web pages analyzer. Main page
`,
content = @`
    @if(UrlRepository.getEntities().isEmpty())
        <br>
        <p>No urls added yet!</p>
    @endif`
)