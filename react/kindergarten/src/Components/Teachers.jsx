import React, { useState, useEffect } from 'react';
import TeacherService from '../API/TeacherService';

function Teachers() {
    const [teachers, setTeachers] = useState([]);

    useEffect(() => {
        async function fetchData() {
            const data = await TeacherService.getAll();
            setTeachers(data);
        }
        fetchData();
    }, []);

    return (
        <div>
            <h2>Teachers</h2>
            <p>This is a table with all of the teachers</p>
            {teachers.length > 0 ? (
                <table>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Phone</th>
                        <th>Skype</th>
                        <th>Group Name</th>
                    </tr>
                    </thead>
                    <tbody>
                    {teachers.map((teacher) => (
                        <tr>
                            <td>{teacher.name}</td>
                            <td>{teacher.phone}</td>
                            <td>{teacher.skype}</td>
                            <td>{teacher.groupName}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : (
                <p>Teachers not found</p>
            )}
        </div>
    );
}

export default Teachers;