@import hexlet.code.model.UrlPage
@import hexlet.code.repository.UrlRepository
@param UrlPage page

@template.otherpages(
page = page,
title = @`
    <title>Domain info</title>
`,
content = @`
    @if(page != null)
        <br>

        <p>Id : ${page.getUrl().getName()}  Name: ${page.getUrl().getCreatedAt()}  </p>
    @endif
`
)