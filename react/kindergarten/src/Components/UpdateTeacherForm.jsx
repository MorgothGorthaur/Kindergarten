import {Button, Form} from "react-bootstrap";
import Input from "../UI/Input/Input";
import {useState, useEffect} from "react";
import TeacherService from "../API/TeacherService";
import LoginService from "../API/LoginService";

const UpdateTeacherForm = ({teacher, tokens, setTokens}) => {
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
            setPassword(teacher.password);
        }
    }, [teacher]);
    const update = (e) => {
        e.preventDefault();
        TeacherService.change(name, phone, skype, email, password, tokens).then(data => {
            console.log(data);
            if (data.message === "authorization or authentication exception") {
                LoginService.refresh(tokens).then(refresh => {
                    if (refresh.hasError) {
                        alert(data.debugMessage)
                    } else {
                        setTokens(refresh, tokens.refresh_token);
                        console.log(refresh);
                        TeacherService.change(name, phone, skype, email, password, refresh).then(d => {
                            validation(d);
                        })
                    }
                });
            } else {
                validation(data);
            }
        });
    };
    const validation = (data) => {
        if(data.debugMessage) alert(data.debugMessage);
    }
    return (
        <Form className="form" onSubmit={update}>
            <Input type="text" placeholder="name" value={name} onChange={e => setName(e.target.value)}/>
            <Input type="text" placeholder="phone" value={phone} onChange={e => setPhone(e.target.value)}/>
            <Input type="text" placeholder="skype" value={skype} onChange={e => setSkype(e.target.value)}/>
            <Input type="text" placeholder="email" value={email} onChange={e => setEmail(e.target.value)}/>
            <Input type="password" placeholder="password" value={password} onChange={e => setPassword(e.target.value)}/>
            <Button type="submit"> update </Button>
        </Form>
    );
};
export default UpdateTeacherForm;