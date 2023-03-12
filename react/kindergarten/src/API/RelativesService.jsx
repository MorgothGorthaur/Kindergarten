import LoginService from "./LoginService";

export default class RelativesService {
    static async callApi(url, requestOptions, tokens, setTokens) {
        try {
            const response = await fetch(url, requestOptions);
            const data = await response.json();
            if (data.message === "authorization or authentication exception") {
                const refresh = await LoginService.refresh(tokens);
                if (refresh.hasError) {
                    alert(data.debugMessage)
                } else {
                    setTokens(refresh, tokens.refresh_token);
                    const secondResponse = await fetch(url, requestOptions);
                    return await secondResponse.json();
                }
            }
            return data;
        } catch (e) {
            console.log(e);
        }
    }

    static async get(tokens, setTokens, id) {
        const requestOptions = {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await this.callApi(`http://localhost:8080/kindergarten/relative/${id}`, requestOptions, tokens, setTokens);
    }
}