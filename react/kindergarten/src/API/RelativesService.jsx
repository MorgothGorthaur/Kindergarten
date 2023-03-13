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

    static async add(tokens, setTokens, kidId, name, phone, address) {
        const requestOptions = {
            method: 'POST',
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + tokens.access_token
            },
            body: JSON.stringify({ name, phone, address})
        };
        return await this.callApi(`http://localhost:8080/kindergarten/relative/${kidId}`, requestOptions, tokens, setTokens);
    }

    static async delete(tokens, setTokens, id, kidId) {
        const requestOptions = {
            method: 'DELETE',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await this.callApi(`http://localhost:8080/kindergarten/relative/${kidId}/${id}`, requestOptions, tokens, setTokens);
    }

    static async update(tokens, setTokens, kidId, id, name, phone, address) {
        const requestOptions = {
            method: 'PATCH',
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + tokens.access_token
            },
            body: JSON.stringify({ id, name, phone, address})
        };
        return await this.callApi(`http://localhost:8080/kindergarten/relative/${kidId}`, requestOptions, tokens, setTokens);
    }
}