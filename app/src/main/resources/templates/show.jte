@import hexlet.code.dto.UrlPage
@import hexlet.code.model.UrlCheck
@import hexlet.code.repository.UrlCheckRepository
@import hexlet.code.util.NamedRoutes
@import java.util.Collection
@import java.util.Collections
@param UrlPage page

@template.allpages(
page = page,
title = @`
    Url analyzer. Url checks info
`,
content = @`
    @if(page != null && page.getFlashText() != null)
            <div class="alert alert-${page.getFlashType()}" role="alert">
                ${page.getFlashText()}
            </div>
    @endif
    @if(page != null)
        <br>
        <h3>Url information:</h3>
        <table class="table">
            <caption>
                Url information:
            </caption>
            <thead>
            <tr>
                <th scope="col">Id</th>
                <th scope="col">Имя</th>
                <th scope="col">Дата создания</th>
            </tr>
            </thead>
            <tbody>
                <tr>
                    <th>${page.getUrl().getId()}</th>
                    <th>${page.getUrl().getName()}</th>
                    <th>${page.getUrl().getCreatedAt().toString()}</th>
                </tr>
            </tbody>
        </table>
    @endif
    <br>
    <form action="${NamedRoutes.urlCheckPath(page.getUrl().getId())}" method="post">
        <div>
            <label>
                <input type="hidden" name="name" value="${page.getUrl().getName()}">
            </label>
        </div>
        <button class="btn btn-primary" type="submit">Check url</button>
    </form>
    <br>

    <h3>Url checks:</h3>
    <table class="table">
        <caption>
            Url checks:
        </caption>
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">Код ответа</th>
            <th scope="col">Title</th>
            <th scope="col">h1</th>
            <th scope="col">description</th>
            <th scope="col">Дата проверки</th>
        </tr>
        </thead>
        <tbody>
        @if(!page.getList().isEmpty())
            @for (UrlCheck element : page.getList().reversed())
                <tr>
                    <th>${element.getId()}</th>
                    <th>${element.getStatusCode()}</th>
                    <th>${element.getTitle()}</th>
                    <th>${element.getH1()}</th>
                    <th>${element.getDescription()}</th>
                    <th>${element.getCreatedAt().toString()}</th>
                </tr>
            @endfor
        @endif
        </tbody>
    </table>
`
)
