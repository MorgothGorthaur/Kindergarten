import {Button, Form} from "react-bootstrap";
import Input from "../UI/Input/Input";
import {useEffect, useState} from "react";
import TeacherService from "../API/TeacherService";

const TeacherForm = ({teacher, tokens, setTokens}) => {
    const [name, setName] = useState('');
    const [phone, setPhone] = useState('');
    const [skype, setSkype] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    useEffect(() => {
        if (teacher) {
            setName(teacher.name);
            setPhone(teacher.phone);
            setSkype(teacher.skype);
            setEmail(teacher.email);
        }
    }, [teacher]);
    const update = (e) => {
        e.preventDefault();
        TeacherService.change(name, phone, skype, email, password, tokens, setTokens).then(data => {
            console.log(data);
            validation(data);
        });
    };
    const add = (event) => {
        event.preventDefault();
        TeacherService.save(name, phone, skype, email, password).then(data => {
            console.log(data);
            validation(data);
        })
    };
    const validation = (data) => {
        if (data.debugMessage) alert(data.debugMessage);
    }
    return (
        <Form className="form" onSubmit={teacher ? update : add}>
            <Input type="text" placeholder="name" value={name} onChange={e => setName(e.target.value)}/>
            <Input type="text" placeholder="phone" value={phone} onChange={e => setPhone(e.target.value)}/>
            <Input type="text" placeholder="skype" value={skype} onChange={e => setSkype(e.target.value)}/>
            <Input type="text" placeholder="email" value={email} onChange={e => setEmail(e.target.value)}/>
            <Input type="password" placeholder="password" value={password} onChange={e => setPassword(e.target.value)}/>
            <Button type="submit"> {teacher ? "update" : "add"} </Button>
        </Form>
    );
};
export default TeacherForm;