@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BuildUrlPage
@param BuildUrlPage page

@template.allpages(
page = page,
title = @`
    Url analyzer. Main page
`,
content = @`
    @if(page != null && page.getErrors() != null)
        @if(page.getFlashText() != null)
            <div class="alert alert-${page.getFlashType()}" role="alert">
                ${page.getFlashText()}
            </div>
        @endif
    @endif
    <form action="${NamedRoutes.urlsPath()}" method="post">
        <div>
            <label>
                <br>
                <p> Add name of domain</p>
                <input type="text" name="url" value="${page.getName()}" />
            </label>
            <input type="submit" value="Add" />
        </div>

    </form>`
)