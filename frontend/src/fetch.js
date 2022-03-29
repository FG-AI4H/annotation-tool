/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

export const callApiWithToken = async(accessToken, apiEndpoint,method = "GET") => {
    const headers = new Headers();
    const bearer = `Bearer ${accessToken}`;

    headers.append("Authorization", bearer);
    headers.append("Content-Type", "application/json")
    headers.append("Accept", "application/json")

    const options = {
        method: method,
        mode: "cors",
        headers: headers
    };

    return fetch(apiEndpoint, options)
        .then((response) =>

            Promise.all([response, response.status === 204 ? '' : response.json()])
        )
        .catch(error => console.log(error));
}


export const postApiWithToken = async(accessToken, apiEndpoint, jsonContent, method) => {
    const headers = new Headers();
    const bearer = `Bearer ${accessToken}`;

    headers.append("Authorization", bearer);
    headers.append("Content-Type", "application/json")
    headers.append("Accept", "application/json")

    const options = {
        method: method,
        mode: "cors",
        headers: headers,
        body: JSON.stringify(jsonContent)
    };

    return fetch(apiEndpoint, options)
        .then((response) => Promise.all([response]))
        .catch(error => console.log(error));
}
