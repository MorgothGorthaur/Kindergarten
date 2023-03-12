import { useState, useEffect } from 'react';
import KidsService from '../API/KidsService';

function KidsWithRelatives({ tokens, setTokens }) {
    const [kids, setKids] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const data = await KidsService.getKidsWithRelatives(tokens, setTokens);
            setKids(data);
        };
        fetchData();
    }, []);

    return (
        <div>
            <h3>Kids with relatives</h3>
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Birth year</th>
                    <th>Relatives</th>
                </tr>
                </thead>
                <tbody>
                {kids.map((kid) => (
                    <tr key={kid.id}>
                        <td>{kid.name}</td>
                        <td>{kid.birthYear}</td>
                        <td>
                            {kid.relatives.length > 0 ? (
                                <table>
                                    <thead>
                                    <tr>
                                        <th>Name</th>
                                        <th>Address</th>
                                        <th>Phone</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {kid.relatives.map((relative) => (
                                        <tr key={relative.id}>
                                            <td>{relative.name}</td>
                                            <td>{relative.address}</td>
                                            <td>{relative.phone}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            ) : (
                                'No relatives found'
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default KidsWithRelatives;