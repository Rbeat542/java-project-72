@import hexlet.code.repository.UrlRepository

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