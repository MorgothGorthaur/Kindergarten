import LoginService from "./LoginService";

export default class GroupService {
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

    static async getGroup(tokens, setTokens) {
        const requestOptions = {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await this.callApi('http://localhost:8080/kindergarten/group', requestOptions, tokens, setTokens);
    }

    static async add(name, maxSize, tokens, setTokens) {
        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + tokens.access_token
            },
            body: JSON.stringify({name, maxSize})
        };
        return await this.callApi('http://localhost:8080/kindergarten/group', requestOptions, tokens, setTokens);
    }

    static async update(name, maxSize, tokens, setTokens) {
        const requestOptions = {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + tokens.access_token
            },
            body: JSON.stringify({name, maxSize})
        };
        return await this.callApi('http://localhost:8080/kindergarten/group', requestOptions, tokens, setTokens);
    }

    static async delete(tokens, setTokens) {
        const requestOptions = {
            method: 'DELETE',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await this.callApi('http://localhost:8080/kindergarten/group', requestOptions, tokens, setTokens);
    }
}