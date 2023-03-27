import LoginService from "./LoginService";

export default class CallApi {
    static async callApi(url, requestOptions, tokens, setTokens) {
        try {
            const response = await fetch(url, requestOptions);
            const data = await response.json();
            if (data.message === "authorization or authentication exception") {
                const refresh = await LoginService.refresh(tokens);
                if (refresh.hasError) {
                    alert(data.debugMessage);
                } else {
                    const newTokens = {
                        access_token: refresh.access_token,
                        refresh_token: tokens.refresh_token
                    };
                    setTokens(newTokens);
                    requestOptions.headers.Authorization = `Bearer ${newTokens.access_token}`;
                    const secondResponse = await fetch(url, requestOptions);
                    const newData = await secondResponse.json();
                    if (newData.debugMessage) alert(newData.debugMessage)
                    else return newData;
                }
            }
            if (data.debugMessage) alert(data.debugMessage)
            else return data;
        } catch (e) {
            console.log(e);
        }
    }
}