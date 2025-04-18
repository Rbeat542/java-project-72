@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BuildUrlPage
@param BuildUrlPage page

@template.otherpages(
page = page,
title = @`
    <title>Add site to database</title>
`,
content = @`
    @if(page != null && page.getErrors() != null)
        @if(page.getFlash() != null)
            <div class="alert alert-danger" role="alert">
                ${page.getFlash()}
            </div>
        @endif
    @endif
    <form action="${NamedRoutes.urlsPath()}" method="post">
        <div>
            <label>
                <p> Add name of domain</p>
                <input type="text" name="url" value="${page.getName()}" />
            </label>
        </div>
        <div>
            <label>
                <input type="hidden" name="createdAt" value="${page.getCreatedAt()}" />
            </label>
        </div>
        <input type="submit" value="Add domain" />
    </form>`
)