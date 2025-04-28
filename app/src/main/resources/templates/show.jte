@import hexlet.code.model.UrlPage
@import hexlet.code.repository.UrlCheckRepository
@import hexlet.code.util.NamedRoutes
@param UrlPage page

@template.otherpages(
page = page,
title = @`
    Web pages analyzer. Url info
`,
content = @`
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
                    <th>${page.getUrl().getCreatedAt()}</th>
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
        !{var list = UrlCheckRepository.getEntities(page.getUrl().getId());}
        @if(!list.isEmpty())
            @for(int i = list.size() - 1; i >= 0; i--)
                <tr>
                    <th>${list.get(i).getId()}</th>
                    <th>${list.get(i).getStatusCode()}</th>
                    <th>${list.get(i).getTitle()}</th>
                    <th>${list.get(i).getH1()}</th>
                    <th>${list.get(i).getDescription()}</th>
                    <th>${list.get(i).getCreatedAt()}</th>
                </tr>
            @endfor
        @endif
        </tbody>
    </table>
`
)
