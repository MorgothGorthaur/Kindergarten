import CallApi from './CallApi'

export default class GroupService {


    static async getGroup(tokens, setTokens) {
        const requestOptions = {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        return await CallApi.callApi('http://localhost:8080/kindergarten/group', requestOptions, tokens, setTokens);
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
        const data = await CallApi.callApi('http://localhost:8080/kindergarten/group', requestOptions, tokens, setTokens);
        if (data) {
            alert(data.debugMessage);
            return {ok: false}
        }
        return {ok: true};
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
        const data = await CallApi.callApi('http://localhost:8080/kindergarten/group', requestOptions, tokens, setTokens);
        if (data) {
            alert(data.debugMessage);
            return {ok: false}
        }
        return {ok: true};
    }

    static async delete(tokens, setTokens) {
        const requestOptions = {
            method: 'DELETE',
            mode: 'cors',
            headers: {
                'Authorization': 'Bearer ' + tokens.access_token
            }
        };
        const data = await CallApi.callApi('http://localhost:8080/kindergarten/group', requestOptions, tokens, setTokens);
        if (data) {
            alert(data.debugMessage);
            return {ok: false}
        }
        return {ok: true};
    }
}