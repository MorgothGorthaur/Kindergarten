import React, { useState, useEffect } from 'react';
import KidsService from '../API/KidsService';

function KidsThatWaitForBirthday({ tokens, setTokens }) {
    const [kids, setKids] = useState([]);

    useEffect(() => {
        async function fetchData() {
            const data = await KidsService.getKidsThatWaitBirth(tokens, setTokens);
            setKids(data);
        }
        fetchData();
    }, [tokens, setTokens]);

    return (
        <div>
            <h3>Kids that wait for birthday</h3>
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Birth Year</th>
                </tr>
                </thead>
                <tbody>
                {kids.map((kid) => (
                    <tr key={kid.id}>
                        <td>{kid.name}</td>
                        <td>{kid.birthYear}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default KidsThatWaitForBirthday;